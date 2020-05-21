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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.reports_fragment.view.*
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.common.extension.setToolbar
import org.wbpbp.preshoes.databinding.ReportsFragmentBinding
import org.wbpbp.preshoes.entity.Commentary
import org.wbpbp.preshoes.entity.Features
import org.wbpbp.preshoes.entity.Report
import java.time.Duration
import java.util.*

class ReportsFragment : BaseFragment<ReportsFragmentBinding>() {
    private val adapter: ReportsRecyclerViewAdapter by lazy {
        ReportsRecyclerViewAdapter(viewModel)
    }

    override val viewModel: ReportsViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.reports_fragment

    override fun initView(root: View) {
        setToolbar(R.id.toolbar_report, R.menu.menu_report)

        with(root.reports_recyclerview) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@ReportsFragment.adapter
        }
    }

    override fun initBinding(binding: ReportsFragmentBinding) {
        binding.vm = viewModel.apply {
            observe(reportClickEvent) {
                it?.let(::showDetailedReport)
            }
        }

        setDummyData()
    }

    private fun showDetailedReport(report: Report) {
        findNavController().navigate(R.id.action_report_detail)
    }











    private fun setDummyData() {
        val f1 = Features(
            score = 44,
            staticHorizontalBiasMerged = 0.45,
            walks = 582,
            horizontalBiasVariationDuringWalkSession = listOf(0.43, 0.43, 0.43, 0.44, 0.43, 0.43, 0.43, 0.45, 0.45, 0.45),
            partialPressureVariationDuringAverageCycle = listOf(
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0),
                listOf(1, 2, 3, 4, 5, 4, 3, 2, 1, 0)
            )
        )

        val c1 = Commentary(
            adviceOnHorizontalBias = "Hmm...."
        )

        val d1 = Duration.ofSeconds(192)

        val r1 = Report(
            id = 1,
            date = Date(),
            duration = d1,
            features = f1,
            commentary = c1
        )

        adapter.setReports(listOf(r1))
    }
}