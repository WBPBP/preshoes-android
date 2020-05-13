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

package org.wbpbp.preshoes.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toast의 wrapper.
 *
 * NOTE:
 * Toast 자체는 android.widget에 속하며 그 wrapper인 Notify 또한 app.common.widget에
 * 속하는 것이 합당해 보이지만, 이 클래스가 data와 app 모듈 모두에서 사용될 것이기 때문에
 * common 모듈에 포함하기로 함.
 */
class Notify(private val context: Context?) {

    fun short(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun short(@StringRes message: Int, vararg formatArgs: Any?) = short(context?.getString(message, *formatArgs))

    fun long(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun long(@StringRes message: Int) = long(context?.getString(message))

    companion object {
        fun short(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        fun long(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}