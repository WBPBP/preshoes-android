package org.wbpbp.preshoes.feature.report

import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.report_detail_fragment.view.*
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.resolveThemeColor
import org.wbpbp.preshoes.databinding.ReportDetailFragmentBinding

class ReportDetailFragment : BaseFragment<ReportDetailFragmentBinding>() {
    override val viewModel: ReportDetailViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.report_detail_fragment

    override fun initView(root: View) {
        initChart(root.horizontal_bias_chart)
    }

    override fun initBinding(binding: ReportDetailFragmentBinding) {
        binding.vm = viewModel
    }

    private fun initChart(chart: LineChart) {
        with(chart) {
            val leftFootData: List<Entry> = listOf(
                Entry(0.5f, 60f),
                Entry(1.0f, 60f),
                Entry(1.5f, 59f),
                Entry(2.0f, 58f),
                Entry(2.5f, 57f),
                Entry(3.0f, 56f),
                Entry(3.5f, 56f),
                Entry(4.0f, 56f),
                Entry(4.5f, 52f),
                Entry(5.0f, 49f)
            )

            val rightFootData: List<Entry> = listOf(
                Entry(0.5f, 44f),
                Entry(1.0f, 41f),
                Entry(1.5f, 42f),
                Entry(2.0f, 45f),
                Entry(2.5f, 47f),
                Entry(3.0f, 48f),
                Entry(3.5f, 51f),
                Entry(4.0f, 52f),
                Entry(4.5f, 52f),
                Entry(5.0f, 53f)
            )


            val leftDataSet = LineDataSet(leftFootData, "왼쪽").apply {
                setValueTextColors(listOf(requireActivity().resolveThemeColor(android.R.attr.textColorPrimary)))
                valueTextSize = 12f
                color = requireActivity().resolveThemeColor(android.R.attr.colorPrimary)
            }
            val rightDataSet = LineDataSet(rightFootData, "오른쪽").apply {
                setValueTextColors(listOf(requireActivity().resolveThemeColor(android.R.attr.textColorPrimary)))
                valueTextSize = 12f
                color = requireActivity().resolveThemeColor(android.R.attr.colorPrimaryDark)
            }

            val dataSets = listOf(leftDataSet, rightDataSet)

            legend.isEnabled = false

            data = LineData(dataSets)
        }
    }
}