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

package org.wbpbp.preshoes.helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import timber.log.Timber
import java.util.*

class BluetoothHelperImpl : BluetoothHelper {
    override fun connectDeviceByName(deviceName: String) = tryConnectDeviceByName(deviceName)

    private fun tryConnectDeviceByName(deviceName: String): BluetoothSocket? {
        val adapter = getBTAdapter().apply {
            cancelDiscovery()
        }

        val deviceFound = adapter.bondedDevices.find { device -> device.name == deviceName }
        if(deviceFound == null) {
            Timber.w("No device with name '${deviceName}' found")
            return null
        }

        return deviceFound.let(::tryConnectSocket)
    }

    private fun tryConnectSocket(device: BluetoothDevice): BluetoothSocket? {
        val thisPhoneUUID = UUID.fromString("preshoes-android")

        return tryConnectSocketWithUUIDSafely(device, thisPhoneUUID)
    }

    private fun tryConnectSocketWithUUIDSafely(device: BluetoothDevice, uuid: UUID): BluetoothSocket? {
        return try {
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()

            socket
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("Failed to connect to device '${device.name}'")

            null
        }
    }

    private fun isBluetoothEnabled(): Boolean = getBTAdapter().isEnabled

    private fun getBTAdapter() = BluetoothAdapter.getDefaultAdapter()
}
