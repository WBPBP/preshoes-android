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

package org.wbpbp.preshoes.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _isDeviceAvailable = MutableLiveData<Boolean>(true)
    val isDeviceAvailable: LiveData<Boolean> = _isDeviceAvailable

    private val _leftBatteryLevel = MutableLiveData<Int>(37)
    val leftBatteryLevel: LiveData<Int> = _leftBatteryLevel

    private val _rightBatteryLevel = MutableLiveData<Int>(64)
    val rightBatteryLevel: LiveData<Int> = _rightBatteryLevel

    private val _isLeftBatteryCharging = MutableLiveData<Boolean>(true)
    val isLeftBatteryCharging: LiveData<Boolean> = _isLeftBatteryCharging

    private val _isRightBatteryCharging = MutableLiveData<Boolean>(false)
    val isRightBatteryCharging: LiveData<Boolean> = _isRightBatteryCharging
}