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
import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.common.util.CombinedLiveData
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.repository.SensorStateRepository

class HomeViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val sensorStateRepo: SensorStateRepository by inject()

    /** At least one device is available */
    val deviceAvailable = CombinedLiveData(
        sensorStateRepo.isLeftDeviceConnected,
        sensorStateRepo.isRightDeviceConnected)
    { left, right ->
        (left ?: false) || (right ?: false)
    }
    /** Both devices are available */
    val deviceComplete = CombinedLiveData(
        sensorStateRepo.isLeftDeviceConnected,
        sensorStateRepo.isRightDeviceConnected)
    { left, right ->
        (left ?: false) && (right ?: false)
    }

    val isLeftDeviceConnected: LiveData<Boolean> = sensorStateRepo.isLeftDeviceConnected
    val isRightDeviceConnected: LiveData<Boolean> = sensorStateRepo.isRightDeviceConnected

    val leftDeviceSensorValue: LiveData<Sample> = sensorStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorStateRepo.rightDeviceSensorValue

    val isLeftBatteryCharging = CombinedLiveData(
        sensorStateRepo.isLeftDeviceConnected,
        sensorStateRepo.isLeftDeviceCharging)
    { available, charging ->
        charging?.takeIf { available ?: false }
    }
    val isRightBatteryCharging = CombinedLiveData(
        sensorStateRepo.isRightDeviceConnected,
        sensorStateRepo.isRightDeviceCharging)
    { available, charging ->
        charging?.takeIf { available ?: false }
    }

    val leftBatteryLevel = CombinedLiveData(
        sensorStateRepo.isLeftDeviceConnected,
        sensorStateRepo.leftDeviceBatteryLevel)
    { available, level ->
        level?.takeIf { available ?: false }
    }
    val rightBatteryLevel = CombinedLiveData(
        sensorStateRepo.isRightDeviceConnected,
        sensorStateRepo.rightDeviceBatteryLevel)
    { available, level ->
        level?.takeIf { available ?: false }
    }

    val leftBatteryLevelText = CombinedLiveData(
        sensorStateRepo.isLeftDeviceConnected,
        sensorStateRepo.leftDeviceBatteryLevel)
    { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
    val rightBatteryLevelText = CombinedLiveData(
        sensorStateRepo.isRightDeviceConnected,
        sensorStateRepo.rightDeviceBatteryLevel)
    { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
}