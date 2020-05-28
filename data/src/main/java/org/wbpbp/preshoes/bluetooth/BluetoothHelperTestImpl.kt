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

import org.wbpbp.preshoes.service.FakeDataGenerator
import org.wbpbp.preshoes.service.FakeDataGenerator.Companion.CHANNEL_LEFT
import org.wbpbp.preshoes.service.FakeDataGenerator.Companion.CHANNEL_RIGHT
import org.wbpbp.preshoes.service.FakeDataGenerator.Companion.STATE_WALKING

class BluetoothHelperTestImpl : BluetoothHelper {
    private var paired = mutableMapOf(
        "PreshoesLeft" to true,
        "PreshoesRight" to true
    )

    private var connected = mutableMapOf(
        "PreshoesLeft" to false,
        "PreshoesRight" to false
    )

    override fun isBluetoothEnabled(): Boolean {
        return true
    }

    override fun isDevicePaired(deviceName: String): Boolean {
        return paired[deviceName] ?: false
    }

    override fun isDeviceConnected(deviceName: String): Boolean {
        return connected[deviceName] ?: false
    }

    override fun connectDevice(
        deviceName: String,
        onConnect: () -> Any?,
        onReceive: (ByteArray) -> Any?,
        onFail: () -> Any?,
        onCancel: () -> Any?
    ) {
        if (connected[deviceName] == true) {
            onFail()
        }

        connected[deviceName] = true

        val generator = FakeDataGenerator().apply {
            state = STATE_WALKING
        }

        val channel = when(deviceName) {
            "PreshoesLeft" -> CHANNEL_LEFT
            "PreshoesRight" -> CHANNEL_RIGHT
            else -> CHANNEL_LEFT
        }

        Thread {
            try {
                onConnect()

                while(true) {
                    onReceive(generator.getNextFake(channel))
                    Thread.sleep(50) // 20 samples a sec
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFail()
            }
        }.start()
    }
}