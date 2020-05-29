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

package org.wbpbp.preshoes.feature.diagnose

import android.view.View
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.databinding.UnifiedDiagnosisFragmentBinding

class UnifiedDiagnosisFragment : BaseFragment<UnifiedDiagnosisFragmentBinding>() {
    override val viewModel: UnifiedDiagnosisViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.unified_diagnosis_fragment

    override fun initView(root: View) {
        // Nothing to do.
    }

    override fun initBinding(binding: UnifiedDiagnosisFragmentBinding) {
        binding.vm = viewModel.apply {
            observe(navigateUpEvent) {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // The recording session must survive on 'onStop'.
        viewModel.onDestroy()
    }
}