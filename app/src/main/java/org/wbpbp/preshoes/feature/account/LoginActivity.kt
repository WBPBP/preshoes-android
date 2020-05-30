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

package org.wbpbp.preshoes.feature.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.login_activity.view.*
import org.wbpbp.preshoes.common.base.BaseActivity
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.databinding.LoginActivityBinding

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()

        initBinding()
        initView(binding.root)
    }

    private fun initBinding() {
        binding = LoginActivityBinding.inflate(layoutInflater).apply {
            lifecycleOwner  = this@LoginActivity
            vm = viewModel.apply {
                start(::finish)
            }
        }

        setContentView(binding.root)
    }

    private fun initView(view: View) {
        with(view.password) {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login()
                }
                false
            }
        }
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}

