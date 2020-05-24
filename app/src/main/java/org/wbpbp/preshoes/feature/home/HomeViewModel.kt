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
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.util.CombinedLiveData

class HomeViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    /** At least one device is available */
    val deviceAvailable = CombinedLiveData(
        sensorDeviceStateRepo.isLeftDeviceConnected,
        sensorDeviceStateRepo.isRightDeviceConnected
    )
    { left, right ->
        (left ?: false) || (right ?: false)
    }
    /** Both devices are available */
    val deviceComplete = CombinedLiveData(
        sensorDeviceStateRepo.isLeftDeviceConnected,
        sensorDeviceStateRepo.isRightDeviceConnected
    )
    { left, right ->
        (left ?: false) && (right ?: false)
    }

    val isLeftDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isLeftDeviceConnected
    val isRightDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isRightDeviceConnected

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    val isLeftBatteryCharging = CombinedLiveData(
        sensorDeviceStateRepo.isLeftDeviceConnected,
        sensorDeviceStateRepo.isLeftDeviceCharging
    )
    { available, charging ->
        charging?.takeIf { available ?: false }
    }
    val isRightBatteryCharging = CombinedLiveData(
        sensorDeviceStateRepo.isRightDeviceConnected,
        sensorDeviceStateRepo.isRightDeviceCharging
    )
    { available, charging ->
        charging?.takeIf { available ?: false }
    }

    val leftBatteryLevel = CombinedLiveData(
        sensorDeviceStateRepo.isLeftDeviceConnected,
        sensorDeviceStateRepo.leftDeviceBatteryLevel
    )
    { available, level ->
        level?.takeIf { available ?: false }
    }
    val rightBatteryLevel = CombinedLiveData(
        sensorDeviceStateRepo.isRightDeviceConnected,
        sensorDeviceStateRepo.rightDeviceBatteryLevel
    )
    { available, level ->
        level?.takeIf { available ?: false }
    }

    val leftBatteryLevelText = CombinedLiveData(
        sensorDeviceStateRepo.isLeftDeviceConnected,
        sensorDeviceStateRepo.leftDeviceBatteryLevel
    )
    { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
    val rightBatteryLevelText = CombinedLiveData(
        sensorDeviceStateRepo.isRightDeviceConnected,
        sensorDeviceStateRepo.rightDeviceBatteryLevel
    )
    { available, batteryLevel ->
        batteryLevel?.takeIf { available ?: false }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }
}