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

import androidx.lifecycle.MutableLiveData
import org.wbpbp.preshoes.entity.SignInModel
import org.wbpbp.preshoes.entity.SignUpModel
import org.wbpbp.preshoes.entity.User
import org.wbpbp.preshoes.repository.UserRepository
import timber.log.Timber

class UserServiceImpl(
    private val api: ApiService,
    private val userRepo: UserRepository
) : UserService {

    private val isLoggedIn = MutableLiveData(false)

    override fun signUp(params: SignUpModel) =
        api.join(params).execute().isSuccessful

    override fun signIn(params: SignInModel?): Boolean {
        val paramToUse = params ?: getSignInParam() ?: return false
        val succeeded = signInInternal(paramToUse)

        isLoggedIn.postValue(succeeded)

        if (succeeded) {
            userRepo.saveUser(
                User(paramToUse.user_email, paramToUse.user_pwd)
            )
        }

        return succeeded
    }

    private fun getSignInParam(): SignInModel? {
        val user = userRepo.getUser() ?: return run {
            Timber.w("No user found!")
            null
        }

        return SignInModel(user.email, user.password)
    }

    private fun signInInternal(params: SignInModel) =
        api.login(params).execute().isSuccessful

    override fun isLoggedIn() = isLoggedIn
}