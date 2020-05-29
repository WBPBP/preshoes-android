package org.wbpbp.preshoes.feature.report

import org.koin.core.inject
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.repository.ReportRepository

class ReportDetailViewModel : BaseViewModel() {
    private val reportRepo: ReportRepository by inject()

    var reportId: Int = -1

    val report: Report
        get() = reportRepo.getReportById(reportId) ?: Report()
}