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

package org.wbpbp.preshoes.service

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import org.wbpbp.preshoes.entity.User
import org.wbpbp.preshoes.entity.model.SignInModel
import org.wbpbp.preshoes.entity.model.SignUpModel
import org.wbpbp.preshoes.repository.UserRepository
import timber.log.Timber

class UserServiceImpl(
    private val api: ApiService,
    private val pref: SharedPreferences,
    private val userRepo: UserRepository
) : UserService {

    private val isLoggedIn = MutableLiveData<Boolean>(null)

    override fun signUp(params: SignUpModel) =
        api.join(params).execute().isSuccessful

    override fun signIn(params: SignInModel?) =
        if (params == null) {
            signInUsingSavedInfo()
        } else {
            signInUsingUserInput(params)
        }

    private fun signInUsingUserInput(params: SignInModel): Boolean {
        val succeeded = signInInternal(params)

        // Prevent duplicated success or failure.
        setLoggedInStatus(state = succeeded, update = false)

        if (succeeded) {
            saveUser(params)
        }

        return succeeded
    }

    private fun signInUsingSavedInfo(): Boolean {
        val params = getSignInParam()
        val succeeded = params?.let(::signInInternal) ?: false

        setLoggedInStatus(state = succeeded, update = true)

        return succeeded
    }

    /**
     * Post state value to isLoggedIn LiveData.
     * If update is true, post the value if it is already set.
     */
    private fun setLoggedInStatus(state: Boolean, update: Boolean) {
        val origin = isLoggedIn.value

        if (!update && (origin == state)) {
            // Do nothing
        } else {
            isLoggedIn.postValue(state)
        }
    }

    private fun saveUser(params: SignInModel) {
        val user = User(params.user_email, params.user_pwd)
        userRepo.saveUser(user)

        pref.edit {
            putString("userName", user.email)
        }

        Timber.i("User $user saved")
    }

    private fun signInInternal(params: SignInModel) =
        api.login(params).execute().isSuccessful

    private fun getSignInParam(): SignInModel? {
        val user = userRepo.getUser() ?: return run {
            Timber.w("No user found!")
            null
        }

        return SignInModel(
            user.email,
            user.password
        )
    }

    override fun logout(): Boolean {
        setLoggedInStatus(state = false, update = false)

        deleteUser()

        return api.logout().execute().isSuccessful
    }

    private fun deleteUser() {
        Realm.getDefaultInstance().executeTransaction {
            it.delete(User::class.java)
        }

        pref.edit {
            putString("userName", null)
        }
    }

    override fun isLoggedIn() = isLoggedIn
}