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

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import timber.log.Timber
import java.util.*

class SocketCreator(private val device: BluetoothDevice) {
    fun createSocket(): BluetoothSocket? {
        return try {
            Timber.i( "1st attempt: trying with UUID...")

            device.createRfcommSocketToServiceRecord(MY_UUID).apply {
                Timber.i("1st attempt succeeded!")
            }
        } catch (e: Exception) {
            Timber.e( "1st attempt failed :(")

            createFallbackSocket()
        }
    }

    private fun createFallbackSocket(): BluetoothSocket? {
        return try {
            Timber.w( "2nd attempt: trying fallback reflection method...")

            createRfcommSocketByReflection().apply {
                Timber.i("2nd attempt succeeded!")
            }
        } catch (e: Exception) {
            Timber.e( "2nd attempt also failed :(")

            null
        }
    }

    private fun createRfcommSocketByReflection(): BluetoothSocket {
        val paramTypes = arrayOf<Class<*>?>(Int::class.javaPrimitiveType)
        val method = device::class.java.getMethod("createRfcommSocket", *paramTypes)

        return method.invoke(device, 1) as BluetoothSocket
    }

    companion object {
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}