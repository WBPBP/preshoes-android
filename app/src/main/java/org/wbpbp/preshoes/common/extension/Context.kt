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

package org.wbpbp.preshoes.common.extension

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import org.wbpbp.preshoes.extension.tryOrNull

fun Context.getColorCompat(colorRes: Int): Int {
    //return black as a default color, in case an invalid color ID was passed in
    return tryOrNull { ContextCompat.getColor(this, colorRes) } ?: Color.BLACK
}

/**
 * Tries to resolve a resource id from the current theme, based on the [attributeId]
 */
fun Context.resolveThemeAttribute(attributeId: Int, default: Int = 0): Int {
    val outValue = TypedValue()
    val wasResolved = theme.resolveAttribute(attributeId, outValue, true)

    return if (wasResolved) outValue.resourceId else default
}

fun Context.resolveThemeColor(attributeId: Int, default: Int = 0): Int {
    val outValue = TypedValue()
    val wasResolved = theme.resolveAttribute(attributeId, outValue, true)

    return if (wasResolved) getColorCompat(outValue.resourceId) else default
}

fun Context.resolveColor(@ColorRes res: Int): Int {
    return this.resources.getColor(res, theme)
}

fun Context.makeToast(@StringRes res: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, res, duration).show()
}

fun Context.makeToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Activity.dismissKeyboard() {
    window.currentFocus?.let { focus ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focus.windowToken, 0)

        focus.clearFocus()
    }
}

fun Context.registerReceiver(action: String, onReceive: (intent: Intent?) -> Unit) {
    registerReceiver(object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onReceive(intent)
        }
    }, IntentFilter(action))
}
