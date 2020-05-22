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

import androidx.lifecycle.Observer
import org.wbpbp.preshoes.entity.SamplePair
import org.wbpbp.preshoes.repository.SampleRepository
import org.wbpbp.preshoes.repository.SensorStateRepository
import org.wbpbp.preshoes.util.CombinedLiveData
import timber.log.Timber

class SampleRepositoryImpl(
    sensorStateRepo: SensorStateRepository
) : SampleRepository {

    private val state = State()
    private val accumulatedSamplePairs: MutableList<SamplePair> = mutableListOf()

    private val samplePairsLiveData = CombinedLiveData(
        sensorStateRepo.leftDeviceSensorValue,
        sensorStateRepo.rightDeviceSensorValue
    ) { leftSample, rightSample ->
        leftSample?.let { left ->
            rightSample?.let { right ->
                SamplePair(state.lastId++, left, right)
            }
        }
    }

    private val observer = Observer<SamplePair?> { pair ->
        pair?.takeIf { state.isRecording }?.let(accumulatedSamplePairs::add)
    }

    override fun startRecording() {
        if (state.isRecording) {
            Timber.d("Already in recording.")
        }

        state.isRecording = true

        samplePairsLiveData.observeForever(observer)
    }

    override fun finishRecording(): List<SamplePair> {
        if (!state.isRecording) {
            return listOf()
        }

        state.isRecording = false

        samplePairsLiveData.removeObserver(observer)

        return accumulatedSamplePairs
    }

    inner class State(
        var isRecording: Boolean = false,
        var lastId: Int = INITIAL_ID
    )

    companion object {
        private const val INITIAL_ID = 1
    }
}