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
            throw Exception("Already recording!")
        }

        sampleRepo.startRecording()
    }

    override fun finishRecordingStandingPressureDistribution() {
        if (!sampleRepo.isRecording()) {
            throw Exception("Not recording!")
        }

        standingRecord = sampleRepo.finishRecording()
    }

    override fun startRecordingWalkingPressureDistribution() {
        if (sampleRepo.isRecording()) {
            throw Exception("Already recording!")
        }

        sampleRepo.startRecording()
    }

    override fun finishRecordingWalkingPressureDistribution() {
        if (!sampleRepo.isRecording()) {
            throw Exception("Not recording!")
        }

        walkingRecord = sampleRepo.finishRecording()
    }

    override fun finishRecordingAndCreateReport() {
        if (sampleRepo.isRecording()) {
            throw Exception("Recording not finished!")
        }

        val record = sampleRepo.finishRecording()

        val features = extractFeatures(record.samplePairs)

        val newReport = Report(
            date = Date(),
            duration = record.duration,
            features = features
        )

        reportRepo.addNewReport(newReport)
    }

    // TODO
    private fun extractFeatures(samples: List<SamplePair>): Features {
        Timber.d("Extract features")

        return Features()
    }
}