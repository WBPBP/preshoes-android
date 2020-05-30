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

import org.wbpbp.preshoes.entity.model.CommentaryModel
import org.wbpbp.preshoes.entity.model.CommentaryRequestModel
import org.wbpbp.preshoes.entity.model.SignInModel
import org.wbpbp.preshoes.entity.model.SignUpModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("/join/addUser")
    fun join(@Body params: SignUpModel): Call<Unit>

    @POST("/login")
    fun login(@Body params: SignInModel): Call<Unit>

    @Headers("Content-Type: application/json")
    @POST("/send/info")
    fun requestReportCommentary(@Body params: CommentaryRequestModel): Call<CommentaryModel>
}