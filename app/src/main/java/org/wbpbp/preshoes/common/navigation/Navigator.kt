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

package org.wbpbp.preshoes.common.navigation

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.feature.account.LoginActivity
import org.wbpbp.preshoes.feature.main.MainActivity
import org.wbpbp.preshoes.feature.settings.SettingsActivity

class Navigator(
    private val context: Context
) {

    fun showLogin() {
        startActivityWithFlag(LoginActivity.callingIntent(context))
    }

    fun showMain() {
        startActivityWithFlag(MainActivity.callingIntent(context))
    }

    fun showSettings() {
        startActivityWithFlag(SettingsActivity.callingIntent(context))
    }

    fun handleOptionMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> let { showSettings(); true }
            else -> false
        }
    }

    private fun startActivityWithFlag(intent: Intent) {
        // on higher version of android
        context.startActivity(intent.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
    }
}