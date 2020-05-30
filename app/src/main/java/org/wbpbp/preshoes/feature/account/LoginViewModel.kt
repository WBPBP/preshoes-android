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
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.entity.SignInModel
import org.wbpbp.preshoes.usecase.SignIn
import org.wbpbp.preshoes.util.Alert
import timber.log.Timber
import java.net.ConnectException

class LoginViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val login: SignIn by inject()
    private val navigator: Navigator by inject()

    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val username = ObservableField<String>()
    val password = ObservableField<String>()

    private lateinit var onFinishActivity: () -> Any?

    fun start(onFinishActivity: () -> Any? = {}) {
        this.onFinishActivity = onFinishActivity

        username.observe(::onLoginFormDataChanged)
        password.observe(::onLoginFormDataChanged)
    }

    fun login() {
        username.get()?.let { name ->
            password.get()?.let { pw ->
                _isLoading.postValue(true)

                login(name, pw)
            } ?: Timber.w("Password is null!")
        } ?: Timber.w("Username is null!")
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
        if (succeeded) {
            navigator.showMain()
            onFinishActivity()
        } else {
            Alert.usual(R.string.fail_wrong_auth)
        }

        onLoginFinished()
    }

    private fun onLoginFail(error: Exception) {
        when (error) {
            is ConnectException -> Alert.usual(R.string.fail_server_connection)
            else -> Alert.usual(R.string.fail_unknown)
        }

        onLoginFinished()
    }

    private fun onLoginFinished() {
        _isLoading.postValue(false)
    }

    private fun onLoginFormDataChanged() {
        Timber.d("Login from data changed!v ${username.get()} and ${password.get()}")

        _loginFormState.postValue(
            when {
                !isUserNameValid() -> LoginFormState(usernameError = str(R.string.invalid_username))
                !isPasswordValid() ->  LoginFormState(passwordError = str(R.string.invalid_password))
                else -> LoginFormState(isDataValid = true)
            }
        )

        Timber.i(_loginFormState.value.toString())
    }

    private fun isUserNameValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username.get() ?: "").matches()
    }

    private fun isPasswordValid(): Boolean {
        return (password.get() ?: "").length > 5
    }

    private fun str(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}
