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
import org.wbpbp.preshoes.bluetooth.ConnectThread
import timber.log.Timber

class BluetoothHelperImpl : BluetoothHelper {
    override fun isConnected(deviceName: String) =
        Thread.getAllStackTraces().keys
            .find { it.name == "ConnectThread-$deviceName" }
            ?.let{ true } ?: false

    override fun findDevice(deviceName: String): BluetoothDevice? {
        val found = getBTAdapter()
            .bondedDevices
            .find { device -> device.name == deviceName }

        return found ?: run {
            Timber.w("No device with name '${deviceName}' found")
            null
        }
    }

    override fun connectDevice(
        device: BluetoothDevice,
        onReceive: (ByteArray) -> Any?,
        onFail: () -> Any?,
        onCancel: () -> Any?
    ) {
        if (!isConnected(device.name)) {
            ConnectThread(device, onReceive, onFail, onCancel).start()
        }
    }

    override fun isBluetoothEnabled(): Boolean = getBTAdapter().isEnabled

    private fun getBTAdapter() = BluetoothAdapter.getDefaultAdapter()
}
