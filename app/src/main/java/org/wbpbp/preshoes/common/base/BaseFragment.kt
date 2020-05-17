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

package org.wbpbp.preshoes.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.common.navigation.Navigator

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {
    abstract val viewModel: ViewModel

    @LayoutRes
    abstract fun getLayoutRes(): Int
    abstract fun initBinding(binding: T)
    abstract fun initView(root: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate(
            inflater,
            getLayoutRes(),
            container,
            false
        ) as T

        return binding.root.apply {
            initView(this)
            initBinding(binding)
            binding.lifecycleOwner = this@BaseFragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navigator: Navigator by inject()

        return navigator.handleOptionMenu(item) || super.onOptionsItemSelected(item)
    }
}