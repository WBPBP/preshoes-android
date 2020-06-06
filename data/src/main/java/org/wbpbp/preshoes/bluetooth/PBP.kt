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

import kotlin.experimental.and

object PBP {
    const val TYPE_SAMPLES: Int = 1
    const val TYPE_BATTERY: Int = 2

    fun isStartByte(byte: Byte): Boolean {
        return (byte.and(0xF0.toByte()) == 0xF0.toByte())
    }

    fun getType(byte: Byte): Int {
        return byte.and(0x0F.toByte()).toInt()
    }
}