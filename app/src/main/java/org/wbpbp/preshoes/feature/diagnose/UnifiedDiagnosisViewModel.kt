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

package org.wbpbp.preshoes.feature.diagnose

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository

class UnifiedDiagnosisViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    val isLeftDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isLeftDeviceConnected
    val isRightDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isRightDeviceConnected

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    private val _phase = MutableLiveData<Int>(0)
    val phase: LiveData<Int> = _phase

    private val _progressMax = MutableLiveData<Int>(100)
    val progressMax: LiveData<Int> = _progressMax

    private val _progress = MutableLiveData<Int>(100)
    val progress: LiveData<Int> = _progress

    private val _helperText = MutableLiveData<String>(context.getString(R.string.description_please_be_ready))
    val helperText: LiveData<String> = _helperText

    private val _isRunning = MutableLiveData<Boolean>(false)
    val isRunning: LiveData<Boolean> = _isRunning

    fun onCenterButtonClick() {

    }

    private fun render() {
        if (isRunning.value == true) {

        }
    }
}