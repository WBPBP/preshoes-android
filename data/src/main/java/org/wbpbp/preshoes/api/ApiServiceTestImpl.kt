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

package org.wbpbp.preshoes.api

import okhttp3.Request
import org.wbpbp.preshoes.entity.model.CommentaryModel
import org.wbpbp.preshoes.entity.model.CommentaryRequestModel
import org.wbpbp.preshoes.entity.model.SignInModel
import org.wbpbp.preshoes.entity.model.SignUpModel
import org.wbpbp.preshoes.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiServiceTestImpl : ApiService {

    override fun join(params: SignUpModel): Call<Unit> {
        Thread.sleep(500)
        return createFakeCallWithFakeResponse(Unit)
    }

    override fun login(params: SignInModel): Call<Unit> {
        Thread.sleep(500)
        return createFakeCallWithFakeResponse(Unit)
    }

    override fun requestReportCommentary(params: CommentaryRequestModel): Call<CommentaryModel> {
        Thread.sleep(500)

        return createFakeCallWithFakeResponse(
            CommentaryModel(
                47,
                "haha",
                "hehe",
                1
            )
        )
    }

    private fun <T> createFakeCallWithFakeResponse(body: T): Call<T> {
        return object: Call<T> {
            override fun execute(): Response<T> {
                return Response.success(body)
            }

            override fun cancel() {}
            override fun clone(): Call<T> { return this }
            override fun enqueue(callback: Callback<T>) {}
            override fun isCanceled(): Boolean { return false }
            override fun isExecuted(): Boolean { return false }
            override fun request(): Request { return Request.Builder().build() }
        }
    }
}