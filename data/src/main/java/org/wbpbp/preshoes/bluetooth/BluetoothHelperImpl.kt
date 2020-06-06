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

package org.wbpbp.preshoes.bluetooth

import android.bluetooth.BluetoothAdapter
import timber.log.Timber

class BluetoothHelperImpl : BluetoothHelper {

    override fun isBluetoothEnabled(): Boolean = getBTAdapter().isEnabled

    override fun isDeviceConnected(deviceName: String) =
        Thread.getAllStackTraces().keys
            .find { it.name == "ConnectThread-$deviceName" }
            ?.let{ true } ?: false

    override fun isDevicePaired(deviceName: String): Boolean {
        val found = getBTAdapter()
            .bondedDevices
            .find { device -> device.name == deviceName } != null

        if (!found) {
            Timber.w("No device with name '${deviceName}' found")
        }

        return found
    }

    private fun findDevice(deviceName: String) =
        getBTAdapter()
            .bondedDevices
            .find { device -> device.name == deviceName }

    override fun connectDevice(
        deviceName: String,
        onConnect: () -> Any?,
        onReceive: (PbpPacket) -> Any?,
        onFail: () -> Any?,
        onCancel: () -> Any?
    ) {
        val device = findDevice(deviceName)

        if (device == null) {
            Timber.w("No such device as ${deviceName}! Check if it is paired first!")
            return
        }

        if (!isDeviceConnected(device.name)) {
            ConnectThread(device, onConnect, onReceive, onFail, onCancel).start()
        }
    }

    private fun getBTAdapter() = BluetoothAdapter.getDefaultAdapter()
}
