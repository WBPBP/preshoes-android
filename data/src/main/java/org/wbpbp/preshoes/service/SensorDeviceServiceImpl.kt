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

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.helper.BluetoothHelper
import org.wbpbp.preshoes.repository.SensorDeviceConnectionRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import timber.log.Timber
import java.io.InputStream

class SensorDeviceServiceImpl(
    private val deviceConnectionRepo: SensorDeviceConnectionRepository,
    private val deviceStateRepo: SensorDeviceStateRepository,
    private val bluetoothHelper: BluetoothHelper
) : SensorDeviceService {

    // TODO remove these all after test
    private var base: Int = 0

    override fun enterRandomState() {
        with(deviceStateRepo) {
            isRightDeviceConnected.postValue(true)
            isLeftDeviceConnected.postValue(/*base % 200 > 100*/true)

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



    override fun connectLeftSensorDevice(deviceName: String): Boolean {
        val socket = bluetoothHelper.connectDeviceByName(deviceName)
        if (socket == null) {
            Timber.w("Failed to connect '${deviceName}' as left sensor device")
            return false
        }

        whenReceivedSomethingFromSocketThenDoThis(socket) {
            Timber.i(it.toString())
        }

        Timber.d("Reading raw data from left sensor device, in background thread.")

        return true
    }

    override fun connectRightSensorDevice(deviceName: String): Boolean {
        val socket = bluetoothHelper.connectDeviceByName(deviceName)
        if (socket == null) {
            Timber.w("Failed to connect '${deviceName}' as right sensor device")
            return false
        }

        whenReceivedSomethingFromSocketThenDoThis(socket) {
            Timber.i(it.toString())
        }

        Timber.d("Reading raw data from right sensor device, in background thread.")

        return true;
    }

    private fun whenReceivedSomethingFromSocketThenDoThis(socket: BluetoothSocket, body: (ByteArray) -> Any?) {
        val toDoWhenNewThreadIsLaunched = Runnable {
            while (true) {
                if (!socket.isConnected) {
                    break
                }

                try {
                    readRawData(socket.inputStream, (0xff).toByte()).let { rawData ->
                        Handler(Looper.getMainLooper()).post { body(rawData) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.e("Failed to read raw data from socket.")

                    break
                }
            }

            Timber.i("Background runnable is done.")
        }

        Thread(toDoWhenNewThreadIsLaunched).start()
    }

    private fun readRawData(inputStream: InputStream, delimiter: Byte): ByteArray {
        val noData = (-1).toByte()
        val bytes: MutableList<Byte> = mutableListOf()

        while (true) {
            inputStream
                .read()
                .toByte()
                .takeIf { it != noData && it != delimiter }
                ?.let(bytes::add)
                ?: break
        }

        return bytes.toByteArray()
    }

    companion object {
        private const val SOCKET_LEFT = 0
        private const val SOCKET_RIGHT = 1
    }
}