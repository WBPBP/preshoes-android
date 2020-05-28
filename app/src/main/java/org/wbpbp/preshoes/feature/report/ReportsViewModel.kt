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

package org.wbpbp.preshoes.feature.report

import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.repository.ReportRepository
import org.wbpbp.preshoes.usecase.DeleteReport
import org.wbpbp.preshoes.util.Alert
import org.wbpbp.preshoes.util.SingleLiveEvent

class ReportsViewModel : BaseViewModel() {
    private val deleteReport: DeleteReport by inject()
    private val reportRepo: ReportRepository by inject()

    val reports = reportRepo.getAllReports()

    val reportClickEvent = SingleLiveEvent<Report>()

    fun showReportDetail(report: Report) {
        reportClickEvent.postValue(report)
    }

    fun deleteReport(id: Int) {
        deleteReport(id) {
            it
                .onSuccess { Alert.usual(R.string.notify_report_deleted) }
                .onError { Alert.usual(R.string.fail_report_delete_failed) }
        }
    }
}