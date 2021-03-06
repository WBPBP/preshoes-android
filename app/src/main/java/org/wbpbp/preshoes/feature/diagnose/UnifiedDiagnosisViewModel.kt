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
import org.wbpbp.preshoes.preference.Config
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.usecase.*
import org.wbpbp.preshoes.util.Alert
import org.wbpbp.preshoes.util.MultipleIntervalLimitedTaskTimer
import org.wbpbp.preshoes.util.SingleLiveEvent
import org.wbpbp.preshoes.util.TimeString
import timber.log.Timber

class UnifiedDiagnosisViewModel : BaseViewModel() {

    private val context: Context by inject()
    private val sensorDeviceStateRepo: SensorDeviceStateRepository by inject()

    private val startStandingRecording: StartStandingRecording by inject()
    private val finishStandingRecording: FinishStandingRecording by inject()
    private val startWalkingRecording: StartWalkingRecording by inject()
    private val finishWalkingRecording: FinishWalkingRecording by inject()
    private val createReport: CreateReport by inject()
    private val syncReport: SyncReport by inject()
    private val haltRecord: HaltRecording by inject()

    private val config: Config by inject()

    val navigateUpEvent = SingleLiveEvent<Unit>()

    val leftDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.leftDeviceConnectionState
    val rightDeviceConnectionState: LiveData<Int> = sensorDeviceStateRepo.rightDeviceConnectionState

    val leftDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.leftDeviceSensorValue
    val rightDeviceSensorValue: LiveData<Sample> = sensorDeviceStateRepo.rightDeviceSensorValue

    private val deviceComplete = sensorDeviceStateRepo.allConnected

    private val _phase = MutableLiveData(PHASE_READY)
    val phase: LiveData<Int> = _phase

    private val _progressMax = MutableLiveData(100L)
    val progressMax: LiveData<Long> = _progressMax

    private val _progress = MutableLiveData(100L)
    val progress: LiveData<Long> = _progress

    private val _helperText = MutableLiveData(context.getString(R.string.description_please_be_ready))
    val helperText: LiveData<String> = _helperText

    private val _isOnGoing = MutableLiveData(false)
    val isOnGoing: LiveData<Boolean> = _isOnGoing

    private val _centerButtonText = MutableLiveData(context.getString(R.string.button_start))
    val centerButtonText: LiveData<String> = _centerButtonText

    private var diagnosisSession: MultipleIntervalLimitedTaskTimer? = null

    fun onCenterButtonClick() {
        val readyToGo = deviceComplete.value ?: false

        if (!readyToGo) {
            Alert.usual(R.string.fail_not_paired)
            return
        }

        when (phase.value) {
            PHASE_READY -> startStaticDiagnosis(config.standingDiagnosisDurationMillis)
            PHASE_STANDING -> startWalkDiagnosis(config.walkingDiagnosisDurationMillis)
            PHASE_WALKING -> finishDiagnosis()
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
            _centerButtonText.postValue(TimeString.millisToMMSS(duration - elapsed))
        }

        diagnosisSession = MultipleIntervalLimitedTaskTimer(
            duration,
            updateProgressRingTask,
            updateButtonText
        ) {
            cancelTimerTasks()
            onFinish()
        }.apply { start() }
    }

    private fun startStaticDiagnosis(duration: Long) {
        startDiagnosis(
            duration,
            onStart = {
                startStandingRecording(Unit) { result ->
                    result
                        .onError { Alert.usual(R.string.fail_to_start_recording) }
                }

                _phase.postValue(PHASE_STANDING)
                _isOnGoing.postValue(true)
                _progressMax.postValue(duration)

                _helperText.postValue(str(R.string.description_static_diagnosis))
            },
            onFinish = {
                 finishStandingRecording(Unit) { result ->
                     result
                         .onError { Alert.usual(R.string.fail_to_finish_recording) }
                 }

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
                startWalkingRecording(Unit) { result ->
                    result
                        .onError { Alert.usual(R.string.fail_to_start_recording) }
                }

                _phase.postValue(PHASE_WALKING)
                _isOnGoing.postValue(true)
                _progressMax.postValue(duration)

                _helperText.postValue(str(R.string.description_walk_diagnosis))
            },
            onFinish = {
                finishWalkingRecording(Unit) { result ->
                    result
                        .onError { Alert.usual(R.string.fail_to_finish_recording) }
                }

                _isOnGoing.postValue(false)
                _progress.postValue(duration)

                _centerButtonText.postValue(str(R.string.button_finish))
                _helperText.postValue(str(R.string.description_diagnosis_done))
            }
        )
    }

    private fun finishDiagnosis() {
        _phase.postValue(PHASE_DONE)

        createReport(Unit) { result ->
            result
                .onSuccess { it?.let(::syncReport) ?: Alert.usual(R.string.fail_report_done) }
                .onError { Alert.usual(R.string.fail_report_done) }
        }

        navigateUpEvent.postValue(Unit)
    }

    private fun syncReport(reportId: Int) {
        syncReport(reportId) {
            it
                .onSuccess { Alert.usual(R.string.notify_report_done) }
                .onError { Alert.usual(R.string.fail_report_done) }
        }
    }

    private fun str(@StringRes id: Int) = context.getString(id)

    fun onDestroy() {
        when (phase.value) {
            PHASE_READY -> {}
            PHASE_STANDING -> onUnexpectedEscape()
            PHASE_WALKING -> onUnexpectedEscape()
            PHASE_DONE -> {}
        }
    }

    private fun onUnexpectedEscape() {
        Timber.w("Unexpected escape from diagnosis!")

        cancelTimerTasks()
        cancelRecording()
    }

    private fun cancelTimerTasks() {
        Timber.d("Cancel timer tasks")

        diagnosisSession?.cancel()
        diagnosisSession = null
    }

    private fun cancelRecording() {
        Timber.d("Cancel recoding: halt")

        haltRecord(Unit) {
            it
                .onSuccess { Alert.usual(R.string.notify_session_canceled) }
                .onError { Alert.usual(R.string.fail_session_cancel_error) }
        }
    }

    companion object {
        const val PHASE_READY = 0
        const val PHASE_STANDING = 1
        const val PHASE_WALKING = 2
        const val PHASE_DONE = 3
    }
}