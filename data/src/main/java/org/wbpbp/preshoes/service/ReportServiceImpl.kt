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

package org.wbpbp.preshoes.service

import com.jaehee.model.FootData
import com.jaehee.model.FootStepPressureProcessor
import com.jaehee.model.StaticPressureProcessor
import io.realm.RealmList
import org.wbpbp.preshoes.entity.Features
import org.wbpbp.preshoes.entity.Record
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.entity.SamplePair
import org.wbpbp.preshoes.repository.ReportRepository
import org.wbpbp.preshoes.repository.SampleRepository
import timber.log.Timber
import java.util.*

class ReportServiceImpl(
    private val sampleRepo: SampleRepository,
    private val reportRepo: ReportRepository
) : ReportService {

    private lateinit var standingRecord: Record
    private lateinit var walkingRecord: Record

    override fun startRecordingStandingPressureDistribution() {
        if (sampleRepo.isRecording()) {
            Timber.w("Already recording!")
            return
        }

        sampleRepo.startRecording()
    }

    override fun finishRecordingStandingPressureDistribution() {
        if (!sampleRepo.isRecording()) {
            Timber.w("Not recording!")
            return
        }

        standingRecord = sampleRepo.finishRecording()
    }

    override fun startRecordingWalkingPressureDistribution() {
        if (sampleRepo.isRecording()) {
            Timber.w("Already recording! It might have been called twice")
            return
        }

        sampleRepo.startRecording()
    }

    override fun finishRecordingWalkingPressureDistribution() {
        if (!sampleRepo.isRecording()) {
            Timber.w("Not recording! It might have been called twice")
            return
        }

        walkingRecord = sampleRepo.finishRecording()
    }

    override fun finishRecordingAndCreateReport(): Int? {
        if (sampleRepo.isRecording()) {
            Timber.w("Recording not finished! It might have been called twice")
            return null
        }

        val date = Date()
        val duration = standingRecord.duration + walkingRecord.duration
        val features = extractFeatures(standingRecord.samplePairs, walkingRecord.samplePairs)

        val newReport = Report(
            date = date,
            duration = duration,
            features = features
        )

        return reportRepo.addNewReport(newReport)
    }

    private fun extractFeatures(
        standingSamples: List<SamplePair>,
        walkingSamples: List<SamplePair>
    ): Features {
        val features = Features()

        extractStaticFeatures(standingSamples, features)
        extractDynamicFeatures(walkingSamples, features)

        return features
    }

    private fun extractStaticFeatures(standingSamples: List<SamplePair>, features: Features) {
        Timber.d("Extracting static features")
        Timber.d("${standingSamples.size} samples for standing!")

        val staticProcessor = StaticPressureProcessor()

        val staticProcessorInput = standingSamples.map {
            FootData(it.id, it.left.values.toIntArray(), it.right.values.toIntArray())
        }.toTypedArray()

        staticProcessor.process(staticProcessorInput)

        val staticResult = staticProcessor.result

        with(features) {
            verticalWeightBiasLeft = staticResult[0]
            verticalWeightBiasRight = staticResult[1]
            horizontalWeightBias = staticResult[2]
            heelPressureDifference = staticResult[3]
        }
    }

    private fun extractDynamicFeatures(walkingSamples: List<SamplePair>, features: Features) {
        Timber.d("Extracting dynamic features")
        Timber.d("${walkingSamples.size} samples for walking!")

        val walkingProcessor = FootStepPressureProcessor()

        val walkingProcessorInput = walkingSamples.map {
            FootData(it.id, it.left.values.toIntArray(), it.right.values.toIntArray())
        }.toTypedArray()

        walkingProcessor.process(walkingProcessorInput)

        val walkingResult = walkingProcessor.result

        // Guard for left
        val leftPressureSafe = walkingResult.leftPressure.takeIf { result ->
            result.all { !it.isNaN() }
        } ?: doubleArrayOf()

        // Guard for right
        val rightPressureSafe = walkingResult.rightPressure.takeIf { result ->
            result.all { !it.isNaN() }
        } ?: doubleArrayOf()

        with(features) {
            walks = walkingResult.step
            samplesInSingleWalkCycleLeft = RealmList(*leftPressureSafe.toTypedArray())
            samplesInSingleWalkCycleRight = RealmList(*rightPressureSafe.toTypedArray())
            horizontalWeightBiasVariationDuringWalkSession = RealmList(*walkingResult.feetWeightBias.toTypedArray())
        }
    }

    override fun addCommentaryOnReport(reportId: Int) {
        reportRepo.addCommentaryOnReport(reportId)
    }

    override fun haltRecording() {
        if (sampleRepo.isRecording()) {
            Timber.i("Diagnosis halt!")
            sampleRepo.finishRecording()
        }
    }
}