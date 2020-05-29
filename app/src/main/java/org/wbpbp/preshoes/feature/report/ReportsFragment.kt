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

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.reports_fragment.view.*
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.common.extension.setToolbar
import org.wbpbp.preshoes.databinding.ReportsFragmentBinding
import org.wbpbp.preshoes.entity.Report

class ReportsFragment : BaseFragment<ReportsFragmentBinding>() {
    private val adapter: ReportsAdapter by lazy {
        ReportsAdapter(viewModel)
    }

    override val viewModel: ReportsViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.reports_fragment

    override fun initView(root: View) {
        setToolbar(R.id.toolbar_report, R.menu.menu_report)

        with(root.reports_recyclerview) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@ReportsFragment.adapter.apply { emptyView = root.empty_view }
        }
    }

    override fun initBinding(binding: ReportsFragmentBinding) {
        binding.vm = viewModel.apply {
            observe(reportClickEvent) {
                it?.let(::showDetailedReport)
            }
        }
    }

    private fun showDetailedReport(report: Report) {
        val bundle = bundleOf("reportId" to report.id)

        findNavController().navigate(R.id.action_report_detail, bundle)
    }

}