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
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository.Companion.STATE_CONNECTED
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository.Companion.STATE_CONNECTING
import org.wbpbp.preshoes.usecase.ConnectDevices
import org.wbpbp.preshoes.util.CombinedLiveData
import timber.log.Timber

class HomeViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val connectDevices: ConnectDevices by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    /** At least one device is available */
    val deviceAvailable = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.rightDeviceConnectionState
    ) { left, right ->
        (left == STATE_CONNECTED) || (right == STATE_CONNECTED)
    }

    /** Both devices are available */
    val deviceComplete = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.rightDeviceConnectionState
    ) { left, right ->
        (left == STATE_CONNECTED) && (right == STATE_CONNECTED)
    }

    val connectingInProgress = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.rightDeviceConnectionState
    ) { left, right ->
        (left == STATE_CONNECTING) || (right == STATE_CONNECTING)
    }

    val leftDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.leftDeviceConnectionState
    val rightDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.rightDeviceConnectionState

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    val isLeftBatteryCharging = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.isLeftDeviceCharging
    ) { leftState, charging ->
        charging?.takeIf { leftState == STATE_CONNECTED }
    }

    val isRightBatteryCharging = CombinedLiveData(
        sensorDeviceStateRepo.rightDeviceConnectionState,
        sensorDeviceStateRepo.isRightDeviceCharging
    ) { rightState, charging ->
        charging?.takeIf { rightState == STATE_CONNECTED }
    }

    val leftBatteryLevel = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.leftDeviceBatteryLevel
    ) { leftState, level ->
        level?.takeIf { leftState == STATE_CONNECTED }
    }

    val rightBatteryLevel = CombinedLiveData(
        sensorDeviceStateRepo.rightDeviceConnectionState,
        sensorDeviceStateRepo.rightDeviceBatteryLevel
    ) { rightState, level ->
        level?.takeIf { rightState == STATE_CONNECTED }
    }

    val leftBatteryLevelText = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceConnectionState,
        sensorDeviceStateRepo.leftDeviceBatteryLevel
    ) { leftState, batteryLevel ->
        batteryLevel?.takeIf { leftState == STATE_CONNECTED }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }

    val rightBatteryLevelText = CombinedLiveData(
        sensorDeviceStateRepo.rightDeviceConnectionState,
        sensorDeviceStateRepo.rightDeviceBatteryLevel
    ) { rightState, batteryLevel ->
        batteryLevel?.takeIf { rightState == STATE_CONNECTED }?.let {
            "$it%"
        } ?: context.getString(R.string.text_information_unavailable)
    }

    fun onConnectButtonClick() {
        connectDevices(Pair("PreshoesLeft", "PreshoesRight")) {
            Timber.i("Result: $it")
        }
    }
}