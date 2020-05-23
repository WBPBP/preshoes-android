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
import java.io.IOException
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
        val thisPhoneUUID = UUID.fromString("1AE1D93B-2FCC-4F4D-9E9E-003F99B06FD7")

        return tryConnectSocketWithUUIDSafely(device, thisPhoneUUID)
    }

    private fun tryConnectSocketWithUUIDSafely(device: BluetoothDevice, uuid: UUID): BluetoothSocket? {
        return try {
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()

            socket
        } catch (e: IOException) {
            Timber.e("1nd attempt failed: ${e.message}")

            try {
                Timber.e( "2nd attempt: trying fallback...")

                val socket = device::class.java.getMethod(
                    "createRfcommSocket", *arrayOf<Class<*>?>(
                        Int::class.javaPrimitiveType
                    )
                ).invoke(device, 1) as BluetoothSocket
                socket.connect()

                socket
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun isBluetoothEnabled(): Boolean = getBTAdapter().isEnabled

    private fun getBTAdapter() = BluetoothAdapter.getDefaultAdapter()
}
