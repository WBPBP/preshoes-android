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
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.login_activity.view.*
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.common.base.BaseActivity
import org.wbpbp.preshoes.common.extension.getViewModel
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.databinding.LoginActivityBinding
import org.wbpbp.preshoes.service.UserService

class LoginActivity : BaseActivity() {

    private val userService: UserService by inject()
    private val navigator: Navigator by inject()

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()

        initBinding()
        initView(binding.root)

        setLoginEventListener()
    }

    private fun initBinding() {
        binding = LoginActivityBinding.inflate(layoutInflater).apply {
            lifecycleOwner  = this@LoginActivity
            vm = viewModel.apply { start() }
        }

        setContentView(binding.root)
    }

    private fun initView(view: View) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

    private fun setLoginEventListener() {
        observe(userService.loggedInEvent()) {
            navigator.showMain()
            finish()
        }
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}

