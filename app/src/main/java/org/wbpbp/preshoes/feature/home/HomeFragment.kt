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

package org.wbpbp.preshoes.feature.home

import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.home_fragment.view.*
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.databinding.HomeFragmentBinding
import org.wbpbp.preshoes.entity.FootPressure

class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    override val viewModel: HomeViewModel by getViewModel()
    private var base: Int = 0

    override fun getLayoutRes() = R.layout.home_fragment

    override fun initView(root: View) {
        // do some

        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                root.pressure_view_left.setSensorValues(getRandomFootPressureValue())
                root.pressure_view_right.setSensorValues(getRandomFootPressureValue())
                handler.postDelayed(this, 100)
            }
        }

        handler.postDelayed(runnable, 100)
    }

    private fun getRandomFootPressureValue(): FootPressure {
        return FootPressure(IntArray(12) {base%3 + (12-it)}).also {
            base++
        }
    }

    override fun initBinding(binding: HomeFragmentBinding) {
        binding.vm = viewModel
    }
}