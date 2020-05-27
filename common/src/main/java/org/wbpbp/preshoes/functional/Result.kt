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

package org.wbpbp.preshoes.functional

/**
 * A generic class that holds a value with its loading status.
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    val succeeded get() = this is Success && data != null

    fun <R> onSuccess(body: (T) -> R): Result<*> {
        if (this is Success && data != null) {
            body(data)
        }
        return this
    }

    fun <R> onError(body: (Exception) -> R): Result<*> {
        if (this is Error) {
            body(exception)
        }
        return this
    }

    fun <R> either(onSuccess: (T) -> R, onError: (Exception) -> R): R =
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
        }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    companion object {
        fun <R> of(body: () -> R): Result<R> {
            return try {
                Success(body())
            } catch (e: Exception) {
                e.printStackTrace()
                Error(e)
            }
        }
    }
}