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

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseFragment
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.setToolbar
import org.wbpbp.preshoes.databinding.HomeFragmentBinding
import org.wbpbp.preshoes.service.SensorDeviceService

class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    override val viewModel: HomeViewModel by getViewModel()

    override fun getLayoutRes() = R.layout.home_fragment

    override fun initView(root: View) {
        setToolbar(R.id.toolbar_home, R.menu.menu_home)
    }

    override fun initBinding(binding: HomeFragmentBinding) {
        binding.vm = viewModel
    }







    // TODO: this is for test
    private val deviceService: SensorDeviceService by inject()

    // TODO: this is for test
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        deviceService.connectLeftSensorDevice("PreshoesLeft")
    }
}