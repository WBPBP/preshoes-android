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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.common.util.CombinedLiveData

class HomeViewModel : BaseViewModel() {
    private val context: Context by inject()

    private val _leftDeviceAvailable = MutableLiveData<Boolean>(true)
    private val _rightDeviceAvailable = MutableLiveData<Boolean>(true)
    private val _leftBatteryLevel = MutableLiveData<Int>(37)
    private val _rightBatteryLevel = MutableLiveData<Int>(64)
    private val _isLeftBatteryCharging = MutableLiveData<Boolean>(true)
    private val _isRightBatteryCharging = MutableLiveData<Boolean>(false)

    val leftDeviceAvailable: LiveData<Boolean> = _leftDeviceAvailable
    val rightDeviceAvailable: LiveData<Boolean> = _rightDeviceAvailable

    /** At least one device is available */
    val deviceAvailable = CombinedLiveData(
        leftDeviceAvailable,
        rightDeviceAvailable) { left, right ->
        (left ?: false) || (right ?: false)
    }

    /** Both devices are available */
    val deviceComplete = CombinedLiveData(
        leftDeviceAvailable,
        rightDeviceAvailable) { left, right ->
        (left ?: false) && (right ?: false)
    }

    val leftBatteryLevel = CombinedLiveData(
        leftDeviceAvailable,
        _leftBatteryLevel) { available, level ->
        level?.takeIf { available ?: false }
    }
    val leftBatteryLevelText = CombinedLiveData(
        leftDeviceAvailable,
        _leftBatteryLevel) { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
    val isLeftBatteryCharging = CombinedLiveData(
        leftDeviceAvailable,
        _isLeftBatteryCharging) { available, charging ->
        charging?.takeIf { available ?: false }
    }

    val rightBatteryLevel = CombinedLiveData(
        rightDeviceAvailable,
        _rightBatteryLevel) { available, level ->
        level?.takeIf { available ?: false }
    }
    val rightBatteryLevelText = CombinedLiveData(
        rightDeviceAvailable,
        _rightBatteryLevel) { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
    val isRightBatteryCharging = CombinedLiveData(
        rightDeviceAvailable,
        _isRightBatteryCharging) { available, charging ->
        charging?.takeIf { available ?: false }
    }

    fun onLeftDevice(connected: Boolean) {
        _leftDeviceAvailable.postValue(connected)
    }
}