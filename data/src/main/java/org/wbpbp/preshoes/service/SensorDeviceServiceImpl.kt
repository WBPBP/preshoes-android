/**
 * Copyright (C) 2020 WBPBP <potados99@gmail.com>
 *
 * This file is part of Preshoes (https://github.com/WBPBP).
 *
 * Preshoes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Preshoes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.wbpbp.preshoes.service

import androidx.lifecycle.MutableLiveData
import org.wbpbp.preshoes.bluetooth.BatteryPacket
import org.wbpbp.preshoes.bluetooth.BluetoothHelper
import org.wbpbp.preshoes.bluetooth.PbpPacket
import org.wbpbp.preshoes.bluetooth.SamplesPacket
import org.wbpbp.preshoes.data.R
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.preference.Config
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository.Companion.STATE_CONNECTED
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository.Companion.STATE_CONNECTING
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository.Companion.STATE_NOT_CONNECTED
import org.wbpbp.preshoes.util.Alert
import timber.log.Timber

class SensorDeviceServiceImpl(
    private val deviceStateRepo: SensorDeviceStateRepository,
    private val bluetoothHelper: BluetoothHelper,
    private val config: Config
) : SensorDeviceService {

    // TODO remove these all after test
    private var base: Int = 0

    // TODO remove these all after test
    override fun enterRandomState() {
        with(deviceStateRepo) {
            rightDeviceConnectionState.postValue(STATE_CONNECTED)
            leftDeviceConnectionState.postValue(/*base % 200 > 100*/STATE_CONNECTED)

            leftDeviceBatteryLevel.postValue(45)
            rightDeviceBatteryLevel.postValue(67)

            isLeftDeviceCharging.postValue(false)
            isRightDeviceCharging.postValue(true)

            leftDeviceSensorValue.postValue(getRandomFootPressureValue())
            rightDeviceSensorValue.postValue(getRandomFootPressureValue())
        }
    }

    private fun getRandomFootPressureValue(): Sample {
        return Sample(List(12) {base%16}).also {
            base++
        }
    }

    override fun connectLeftSensorDevice(deviceName: String) =
        connectSensorDevice(
            deviceName,
            deviceStateRepo.leftDeviceConnectionState,
            deviceStateRepo.leftDeviceSensorValue,
            deviceStateRepo.leftDeviceBatteryLevel
        )

    override fun connectRightSensorDevice(deviceName: String) =
        connectSensorDevice(
            deviceName,
            deviceStateRepo.rightDeviceConnectionState,
            deviceStateRepo.rightDeviceSensorValue,
            deviceStateRepo.rightDeviceBatteryLevel
        )

    private fun connectSensorDevice(
        deviceName: String,
        connectionStateLiveData: MutableLiveData<Int>,
        sensorValueLiveData: MutableLiveData<Sample>,
        batteryLevelLiveData: MutableLiveData<Int>
    ): Boolean {
        if (!bluetoothHelper.isBluetoothEnabled()) {
            Alert.usual(R.string.fail_bt_off)
            Timber.w("Bluetooth not enabled!")
            return false
        }

        if (!bluetoothHelper.isDevicePaired(deviceName)) {
            Alert.usual(R.string.fail_not_paired)
            Timber.w("No such device as $deviceName!")
            return false
        }

        if (bluetoothHelper.isDeviceConnected(deviceName)) {
            Alert.usual(R.string.fail_already_connected)
            Timber.w("Device $deviceName already connected!")
            return false
        }

        val onConnect = {
            Timber.d("onConnect: device is connected")

            connectionStateLiveData.postValue(STATE_CONNECTED)
        }

        val onReceive = { packet: PbpPacket ->
            when (packet) {
                is SamplesPacket -> setData(packet.samples, sensorValueLiveData)
                is BatteryPacket -> setBattery(packet.level, batteryLevelLiveData)
            }
        }

        val onFail = {
            Timber.w("onFail: device failed")
            Alert.usual(R.string.fail_disconnected)

            connectionStateLiveData.postValue(STATE_NOT_CONNECTED)
        }

        bluetoothHelper.connectDevice(deviceName, onConnect, onReceive, onFail).also {
            connectionStateLiveData.postValue(STATE_CONNECTING)
        }

        Timber.d("Trying to read raw data from ${deviceName}, in background thread")

        return true
    }

    private fun setData(data: List<Int>, destination: MutableLiveData<Sample>) {
        val sample = Sample(data)

        destination.postValue(sample)
    }

    private fun setBattery(level: Int, destination: MutableLiveData<Int>) {
        destination.postValue(level)
    }
}