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

import io.realm.Realm
import io.realm.RealmResults
import org.wbpbp.preshoes.entity.Commentary
import org.wbpbp.preshoes.entity.Features
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.entity.model.CommentaryRequestModel
import org.wbpbp.preshoes.repository.ReportRepository
import org.wbpbp.preshoes.service.ApiService
import timber.log.Timber

class ReportRepositoryImpl(
    private val api: ApiService
) : ReportRepository {

    override fun addNewReport(report: Report): Int? {
        Timber.d("Add new report!")

        val newId = getLastReportId()?.let { it.toInt() + 1 } ?: 0

        Realm.getDefaultInstance().executeTransaction {
            it.copyToRealm(report.apply { id = newId })
        }

        Timber.d("Report copied to realm")

        return newId
    }

    private fun getLastReportId() = Realm.getDefaultInstance().where(Report::class.java).max("id")

    override fun getAllReports(): RealmResults<Report> {
        return Realm.getDefaultInstance()
            .where(Report::class.java)
            .findAll()
    }

    override fun getReportById(id: Int): Report? {
        return Realm.getDefaultInstance()
            .where(Report::class.java)
            .equalTo("id", id)
            .findFirst()
    }

    override fun addCommentaryOnReport(id: Int) {
        val report = getReportById(id) ?: return run {
            Timber.w("No report with id $id found!")
        }

        val features = report.features ?: return run {
            Timber.w("No features in report with id $id!")
        }

        var commentary = getCommentary(features) ?: return run {
            Timber.w("No commentary secured!")
        }

        Realm.getDefaultInstance().executeTransaction {
            // An object you want to set as a linked object
            // must also be a managed RealmObject as well.
            commentary = it.copyToRealm(commentary)

            report.commentary = commentary
            it.copyToRealmOrUpdate(report)
        }

        Timber.i("Comment added for report with id $id")
    }

    private fun getCommentary(features: Features): Commentary? {
        val requestModel = CommentaryRequestModel(
            verticalWeightBias_Left = features.verticalWeightBiasLeft,
            verticalWeightBias_Right = features.verticalWeightBiasRight,
            horizontalWeightBias = features.horizontalWeightBias,
            heelPressureDifference = features.heelPressureDifference,
            leftPressure = features.samplesInSingleWalkCycleLeft,
            rightPressure = features.samplesInSingleWalkCycleRight
        )

        val response = api.requestReportCommentary(requestModel).execute()

        if (!response.isSuccessful) {
            Timber.e("Getting commentary failed: ${response.code()}")
            return null
        }

        val body = response.body() ?: run {
            Timber.e("Empty body for commentary request!")
            return null
        }

        return Commentary(
            score = body.percent,
            adviceOnStandingHabits = body.staticPressureRes,
            adviceOnWalkingHabits = body.gaitComment,
            possibleMedicalProblem = body.diseaseNum
        )
    }

    override fun deleteReportById(id: Int) {
        Realm.getDefaultInstance().executeTransaction {
            val report = it.where(Report::class.java).equalTo("id", id).findFirst()

            report?.features?.deleteFromRealm()
            report?.commentary?.deleteFromRealm()
            report?.deleteFromRealm()
        }
    }
}