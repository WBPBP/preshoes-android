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
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.BaseViewModel
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.entity.SignInModel
import org.wbpbp.preshoes.usecase.SignIn

class LoginViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val login: SignIn by inject()

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val username = ObservableField<String>()
    val password = ObservableField<String>()

    fun init() {
        username.observe(::loginDataChanged)
        password.observe(::loginDataChanged)
    }

    fun login() {
        login(username.toString(), password.toString())
    }

    private fun login(email: String, password: String) {
        val params = SignInModel(email, password)

        login(params) {
            it
                .onSuccess(::onLoginResult)
                .onError(::onLoginFail)
        }
    }

    private fun onLoginResult(succeeded: Boolean) {
        //
        onLoginFinished()
    }

    private fun onLoginFail(error: Exception) {
        //
        onLoginFinished()
    }

    private fun onLoginFinished() {
        _isLoading.postValue(false)
    }

    private fun loginDataChanged() {
        if (!isUserNameValid(username.toString())) {
            _loginFormState.value = LoginFormState(usernameError = str(R.string.invalid_username))
        } else if (!isPasswordValid(password.toString())) {
            _loginFormState.value = LoginFormState(passwordError = str(R.string.invalid_password))
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun str(@StringRes resId: Int) : String {
        return context.getString(resId)
    }
}
