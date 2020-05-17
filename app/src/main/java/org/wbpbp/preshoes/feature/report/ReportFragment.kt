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
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.setToolbar
import org.wbpbp.preshoes.databinding.ReportFragmentBinding

class ReportFragment : BaseFragment<ReportFragmentBinding>() {
    override val viewModel: ReportViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.report_fragment

    override fun initView(root: View) {
        setToolbar(R.id.toolbar_report, R.menu.menu_report)
    }

    override fun initBinding(binding: ReportFragmentBinding) {
        binding.vm = viewModel
    }
}