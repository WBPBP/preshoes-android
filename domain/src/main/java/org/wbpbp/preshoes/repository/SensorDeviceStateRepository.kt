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

package org.wbpbp.preshoes.repository

import androidx.lifecycle.MutableLiveData
import org.wbpbp.preshoes.entity.Sample

interface SensorDeviceStateRepository {
    // Need to be set from activity.

    val isLeftDeviceConnected: MutableLiveData<Boolean>
    val isRightDeviceConnected: MutableLiveData<Boolean>

    val leftDeviceSensorValue: MutableLiveData<Sample>
    val rightDeviceSensorValue: MutableLiveData<Sample>

    val isLeftDeviceCharging: MutableLiveData<Boolean>
    val isRightDeviceCharging: MutableLiveData<Boolean>

    val leftDeviceBatteryLevel: MutableLiveData<Int>
    val rightDeviceBatteryLevel: MutableLiveData<Int>
}