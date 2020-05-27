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

package org.wbpbp.preshoes.storage

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import org.wbpbp.preshoes.entity.Record
import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.entity.SamplePair
import org.wbpbp.preshoes.repository.SampleRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.util.CombinedLiveData
import timber.log.Timber
import java.util.*

class SampleRepositoryImpl(
    sensorDeviceStateRepo: SensorDeviceStateRepository
) : SampleRepository {

    private val state = State()
    private val accumulatedSamplePairs: MutableList<SamplePair> = mutableListOf()
    private val handler = Handler(Looper.getMainLooper())

    private val samplePairsLiveData = CombinedLiveData(
        sensorDeviceStateRepo.leftDeviceSensorValue,
        sensorDeviceStateRepo.rightDeviceSensorValue
    ) { leftSample, rightSample ->
        val left = leftSample ?: Sample(listOf())
        val right = rightSample ?: Sample(listOf())

        SamplePair(state.lastId++, left, right)
    }

    private val observer = Observer<SamplePair?> { pair ->
        pair?.takeIf { state.isRecording }?.let {
            Timber.d("Collecting $it")

            accumulatedSamplePairs.add(it)
        }
    }

    override fun startRecording() {
        if (state.isRecording) {
            throw Exception("Already in recording.")
        }

        state.isRecording = true
        state.startTime = Date().time

        handler.post {
            samplePairsLiveData.observeForever(observer)
        }
    }

    override fun finishRecording(): Record {
        if (!state.isRecording) {
            throw Exception("Not in recording.")
        }

        handler.post {
            samplePairsLiveData.removeObserver(observer)
        }

        state.isRecording = false

        val elapsed = Date().time - state.startTime

        return Record(
            elapsed,
            accumulatedSamplePairs
        )
    }

    override fun isRecording() = state.isRecording

    inner class State(
        var isRecording: Boolean = false,
        var startTime: Long = 0,
        var lastId: Int = INITIAL_ID
    )

    companion object {
        private const val INITIAL_ID = 1
    }
}