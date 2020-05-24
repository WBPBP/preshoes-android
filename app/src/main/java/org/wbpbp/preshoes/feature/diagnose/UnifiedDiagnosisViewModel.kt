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
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.service.FakeDataGenerator
import org.wbpbp.preshoes.util.SingleLiveEvent
import java.util.*

class UnifiedDiagnosisViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    // TODO for display
    private val generator: FakeDataGenerator by inject()

    val navigateUpEvent = SingleLiveEvent<Unit>()

    val isLeftDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isLeftDeviceConnected
    val isRightDeviceConnected: LiveData<Boolean> = sensorDeviceStateRepo.isRightDeviceConnected

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    private val _phase = MutableLiveData<Int>(PHASE_READY)
    val phase: LiveData<Int> = _phase

    private val _progressMax = MutableLiveData<Int>(100)
    val progressMax: LiveData<Int> = _progressMax

    private val _progress = MutableLiveData<Int>(100)
    val progress: LiveData<Int> = _progress

    private val _helperText = MutableLiveData<String>(context.getString(R.string.description_please_be_ready))
    val helperText: LiveData<String> = _helperText

    private val _isOnGoing = MutableLiveData<Boolean>(false)
    val isOnGoing: LiveData<Boolean> = _isOnGoing

    private val _centerButtonText = MutableLiveData<String>(context.getString(R.string.button_start))
    val centerButtonText: LiveData<String> = _centerButtonText

    fun onCenterButtonClick() {
        when (phase.value) {
            PHASE_READY -> startStaticDiagnosis()
            PHASE_STAND -> startWalkDiagnosis()
            PHASE_WALK -> finishDiagnosis()
        }
    }

    private fun startStaticDiagnosis() {
        _isOnGoing.postValue(true)

        val duration = 5
        var timeLeft = duration

        _helperText.postValue(str(R.string.description_static_diagnosis))
        _progressMax.postValue(duration)
        _phase.postValue(PHASE_STAND)

        generator.state = FakeDataGenerator.STATE_STANDING

        val task = object: TimerTask() {
            override fun run() {
                _progress.postValue(timeLeft)
                _centerButtonText.postValue("00:0${timeLeft}")
                if (timeLeft-- <= 0) {
                    _progress.postValue(duration)
                    _isOnGoing.postValue(false)
                    _centerButtonText.postValue(str(R.string.button_resume))
                    _helperText.postValue(str(R.string.description_keep_next_is_walk))
                    this.cancel()
                }
            }
        }

        Timer().schedule(task, 0, 1000)
    }

    private fun startWalkDiagnosis() {
        _isOnGoing.postValue(true)

        val duration = 20
        var timeLeft = duration

        _helperText.postValue(str(R.string.description_walk_diagnosis))
        _progressMax.postValue(duration)
        _phase.postValue(PHASE_WALK)

        generator.state = FakeDataGenerator.STATE_WALKING

        val task = object: TimerTask() {
            override fun run() {
                _progress.postValue(timeLeft)
                _centerButtonText.postValue("00:${timeLeft.toString().padStart(2, '0')}")
                if (timeLeft-- <= 0) {
                    generator.state = FakeDataGenerator.STATE_STANDING

                    _progress.postValue(duration)
                    _isOnGoing.postValue(false)
                    _centerButtonText.postValue(str(R.string.button_finish))
                    _helperText.postValue(str(R.string.description_diagnosis_done))
                    this.cancel()
                }
            }
        }

        Timer().schedule(task, 0, 1000)

    }

    private fun finishDiagnosis() {


    }

    private fun str(@StringRes id: Int) = context.getString(id)

    companion object {
        private const val PHASE_READY = 0
        private const val PHASE_STAND = 1
        private const val PHASE_WALK = 2
    }
}