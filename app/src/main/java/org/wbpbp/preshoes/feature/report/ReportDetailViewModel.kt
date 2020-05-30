package org.wbpbp.preshoes.feature.report

import android.content.Context
import org.koin.core.inject
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.repository.ReportRepository
import org.wbpbp.preshoes.util.TimeString
import java.text.DateFormat

class ReportDetailViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val reportRepo: ReportRepository by inject()

    private lateinit var report: Report

    fun startWithReportId(reportId: Int) {
        report = reportRepo.getReportById(reportId) ?: Report()
    }

    // Upper
    fun getDateString(): String = DateFormat.getDateInstance(DateFormat.FULL).format(report.date)
    fun getAdviceOnStandingHabits() = report.commentary?.adviceOnStandingHabits
    fun getAdviceOnWalkingHabits() = report.commentary?.adviceOnWalkingHabits
    fun getMedicalPredictionDrawable() = MedicalProblemVisualizer.getDrawableOfPossibleMedicalProblem(context, report.commentary?.possibleMedicalProblem ?: -1)
    fun getMedicalPredictionName() = MedicalProblemVisualizer.getNameOfPossibleMedicalProblem(report.commentary?.possibleMedicalProblem ?: -1)

    // Lower
    fun getChartData() = report.features?.horizontalWeightBiasVariationDuringWalkSession?.toList() ?: listOf()
    fun getScore() = String.format("%d", report.commentary?.score ?: 0)
    fun getWalks() = String.format("%d", report.features?.walks ?: 0)
    fun getHorizontalBias() = String.format("%.1f", report.features?.horizontalWeightBias ?: 0.0)
    fun getDuration() = TimeString.millisToMMSS(report.duration)
}