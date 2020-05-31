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

package org.wbpbp.preshoes.feature.main

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.service.UserService
import org.wbpbp.preshoes.usecase.SignIn
import org.wbpbp.preshoes.util.Alert
import timber.log.Timber
import java.net.ConnectException

class SplashActivity : AppCompatActivity() {

    private val login: SignIn by inject()
    private val userService: UserService by inject()
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLoginEventListener()

        Handler().postDelayed(::doLogin, 200)
    }

    private fun setLoginEventListener() {
        observe(userService.loggedInEvent()) {
            navigator.showMain()
            finish()
        }

        observe(userService.loginNeededEvent()) {
            navigator.showLogin()
            finish()
        }
    }

    private fun doLogin() {
        login(null) {
            it
                .onSuccess(::onLoginResult)
                .onError(::onLoginFail)
        }
    }

    private fun onLoginResult(succeeded: Boolean) {
        if (succeeded) {
            Timber.i("Login succeeded with saved user data. Showing main activity")
        } else {
            Timber.i("No saved user data in local. Showing login activity")
        }
    }

    private fun onLoginFail(error: Exception) {
        when (error) {
            is ConnectException -> Alert.usual(R.string.fail_server_connection)
            else -> Alert.usual(R.string.fail_unknown)
        }
    }
}
