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
import org.wbpbp.preshoes.entity.SignUpModel
import org.wbpbp.preshoes.usecase.SignUp
import org.wbpbp.preshoes.util.Alert
import timber.log.Timber
import java.net.ConnectException

class JoinViewModel : BaseViewModel() {
    private val context: Context by inject()
    private val join: SignUp by inject()

    private val _joinFormState = MutableLiveData<LoginFormState>()
    val joinFormState: LiveData<LoginFormState> = _joinFormState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val username = ObservableField<String>()
    val password = ObservableField<String>()

    private lateinit var onFinishActivity: () -> Any?

    fun start(onFinishActivity: () -> Any? = {}) {
        this.onFinishActivity = onFinishActivity

        username.observe(::onJoinFormDataChanged)
        password.observe(::onJoinFormDataChanged)
    }

    fun join() {
        username.get()?.let { name ->
            password.get()?.let { pw ->
                _isLoading.postValue(true)

                join(name, pw)
            } ?: Timber.w("Password is null!")
        } ?: Timber.w("Username is null!")
    }

    private fun join(email: String, password: String) {
        val params = SignUpModel(email, password)

        join(params) {
            it
                .onSuccess(::onJoinResult)
                .onError(::onJoinFail)
        }
    }

    private fun onJoinResult(succeeded: Boolean) {
        if (succeeded) {
            Alert.usual(R.string.notify_join_success)
            onFinishActivity()
        } else {
            Alert.usual(R.string.fail_wrong_join_request)
        }

        onJoinFinished()
    }

    private fun onJoinFail(error: Exception) {
        when (error) {
            is ConnectException -> Alert.usual(R.string.fail_server_connection)
            else -> Alert.usual(R.string.fail_unknown)
        }

        onJoinFinished()
    }

    private fun onJoinFinished() {
        _isLoading.postValue(false)
    }

    private fun onJoinFormDataChanged() {
        _joinFormState.postValue(
            when {
                !isUserNameValid() -> LoginFormState(usernameError = str(R.string.invalid_username))
                !isPasswordValid() ->  LoginFormState(passwordError = str(R.string.invalid_password))
                else -> LoginFormState(isDataValid = true)
            }
        )

        Timber.i(_joinFormState.value.toString())
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