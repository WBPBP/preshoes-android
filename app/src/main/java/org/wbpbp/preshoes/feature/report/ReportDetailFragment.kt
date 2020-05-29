package org.wbpbp.preshoes.feature.report

import android.view.View
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.databinding.ReportDetailFragmentBinding

class ReportDetailFragment : BaseFragment<ReportDetailFragmentBinding>() {
    override val viewModel: ReportDetailViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.report_detail_fragment

    override fun initView(root: View) {
        // Nothing to do.
    }

    override fun initBinding(binding: ReportDetailFragmentBinding) {
        binding.vm = viewModel.apply {
            startWithReportId(getReportId())
        }
    }

    private fun getReportId() = arguments?.getInt("reportId", -1) ?: -1
}