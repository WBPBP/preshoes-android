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
import org.wbpbp.preshoes.util.Fail
import org.wbpbp.preshoes.util.MultipleIntervalLimitedTaskTimer
import org.wbpbp.preshoes.util.SingleLiveEvent
import java.util.concurrent.TimeUnit

class UnifiedDiagnosisViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    val navigateUpEvent = SingleLiveEvent<Unit>()

    val leftDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.leftDeviceConnectionState
    val rightDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.rightDeviceConnectionState

    private val deviceComplete = sensorDeviceStateRepo.allConnected

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    private val _phase = MutableLiveData<Int>(PHASE_READY)
    val phase: LiveData<Int> = _phase

    private val _progressMax = MutableLiveData<Long>(100L)
    val progressMax: LiveData<Long> = _progressMax

    private val _progress = MutableLiveData<Long>(100L)
    val progress: LiveData<Long> = _progress

    private val _helperText = MutableLiveData<String>(context.getString(R.string.description_please_be_ready))
    val helperText: LiveData<String> = _helperText

    private val _isOnGoing = MutableLiveData<Boolean>(false)
    val isOnGoing: LiveData<Boolean> = _isOnGoing

    private val _centerButtonText = MutableLiveData<String>(context.getString(R.string.button_start))
    val centerButtonText: LiveData<String> = _centerButtonText

    private var diagnosisSession: MultipleIntervalLimitedTaskTimer? = null

    fun onCenterButtonClick() {
        val readyToGo = deviceComplete.value ?: false

        if (!readyToGo) {
            Fail.usual(R.string.fail_not_paired)
            return
        }

        when (phase.value) {
            PHASE_READY -> startStaticDiagnosis(5000L /* 5 sec */)
            PHASE_STAND -> startWalkDiagnosis(10000L /* 10 sec */)
            PHASE_WALK -> finishDiagnosis()
        }
    }

    private fun startDiagnosis(duration: Long, onStart: () -> Any?, onFinish: () -> Any?) {
        onStart()

        // Updating progress ring happens every 0.01 seconds
        val updateProgressRingTask = MultipleIntervalLimitedTaskTimer.Task(10L) { elapsed ->
            _progress.postValue(duration - elapsed)
        }

        // Updating button text(time left) happens every 1 seconds
        val updateButtonText = MultipleIntervalLimitedTaskTimer.Task(1000L) { elapsed ->
            _centerButtonText.postValue(getMMSSLabel(duration - elapsed))
        }

        diagnosisSession = MultipleIntervalLimitedTaskTimer(
            duration,
            updateProgressRingTask,
            updateButtonText
        ) {
            clearSession()
            onFinish()
        }.apply { start() }
    }

    private fun startStaticDiagnosis(duration: Long) {
        startDiagnosis(
            duration,
            onStart = {
                _phase.postValue(PHASE_STAND)
                _isOnGoing.postValue(true)
                _progressMax.postValue(duration)

                _helperText.postValue(str(R.string.description_static_diagnosis))
            },
            onFinish = {
                _isOnGoing.postValue(false)
                _progress.postValue(duration)

                _centerButtonText.postValue(str(R.string.button_resume))
                _helperText.postValue(str(R.string.description_keep_next_is_walk))
            }
        )
    }

    private fun startWalkDiagnosis(duration: Long) {
        startDiagnosis(
            duration,
            onStart = {
                _phase.postValue(PHASE_WALK)
                _isOnGoing.postValue(true)
                _progressMax.postValue(duration)

                _helperText.postValue(str(R.string.description_walk_diagnosis))

            },
            onFinish = {
                _isOnGoing.postValue(false)
                _progress.postValue(duration)

                _centerButtonText.postValue(str(R.string.button_finish))
                _helperText.postValue(str(R.string.description_diagnosis_done))
            }
        )
    }

    private fun finishDiagnosis() {
        navigateUpEvent.postValue(Unit)
    }

    private fun getMMSSLabel(milliSec: Long): String {
        val m = TimeUnit.MILLISECONDS.toMinutes(milliSec) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSec))

        val s = TimeUnit.MILLISECONDS.toSeconds(milliSec) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSec))

        return String.format("%02d:%02d", m, s)
    }

    private fun str(@StringRes id: Int) = context.getString(id)

    fun clearSession() {
        diagnosisSession?.cancel()
        diagnosisSession = null
    }

    companion object {
        private const val PHASE_READY = 0
        private const val PHASE_STAND = 1
        private const val PHASE_WALK = 2
    }
}