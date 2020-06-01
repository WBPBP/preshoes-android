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

import io.realm.Realm
import org.wbpbp.preshoes.entity.User
import org.wbpbp.preshoes.entity.model.SignInModel
import org.wbpbp.preshoes.entity.model.SignUpModel
import org.wbpbp.preshoes.repository.UserRepository
import org.wbpbp.preshoes.util.SingleLiveEvent
import timber.log.Timber

class UserServiceImpl(
    private val api: ApiService,
    private val userRepo: UserRepository
) : UserService {

    private val loggedIn = SingleLiveEvent<Unit>()
    private val loggedOut = SingleLiveEvent<Unit>()
    private val loginNeeded = SingleLiveEvent<Unit>()

    override fun signUp(params: SignUpModel) =
        api.join(params).execute().let {
            Timber.i("SignUp response code: ${it.code()}")
            it.isSuccessful
        }

    override fun signIn(params: SignInModel?) =
        if (params == null) {
            signInUsingSavedInfo()
        } else {
            signInUsingUserInput(params)
        }

    private fun signInUsingUserInput(params: SignInModel): Boolean {
        val succeeded = signInInternal(params)

        if (succeeded) {
            loggedIn.postValue(Unit)
            saveUser(params)
        }

        return succeeded
    }

    private fun signInUsingSavedInfo(): Boolean {
        val params = getSignInParam()
        val succeeded = params?.let(::signInInternal) ?: false

        if (succeeded) {
            loggedIn.postValue(Unit)
        } else {
            loginNeeded.postValue(Unit)
        }

        return succeeded
    }

    private fun saveUser(params: SignInModel) {
        val user = User(params.user_email, params.user_pwd)
        userRepo.saveUser(user)

        Timber.i("User $user saved")
    }

    private fun signInInternal(params: SignInModel) =
        api.login(params).execute().let {
            Timber.i("SignIn response code: ${it.code()}")
            it.isSuccessful
        }

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
        loggedOut.postValue(Unit)
        loginNeeded.postValue(Unit)

        deleteUser()

        return api.logout().execute().isSuccessful
    }

    private fun deleteUser() {
        Realm.getDefaultInstance().executeTransaction {
            it.delete(User::class.java)
        }
    }

    override fun loggedInEvent() = loggedIn
    override fun loggedOutEvent() = loggedOut
    override fun loginNeededEvent() = loginNeeded
}