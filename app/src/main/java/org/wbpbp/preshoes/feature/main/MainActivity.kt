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

package org.wbpbp.preshoes.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.base.NavigationActivity
import org.wbpbp.preshoes.common.base.NavigationHostFragment
import org.wbpbp.preshoes.common.extension.observe
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.common.navigation.rootDestinations
import org.wbpbp.preshoes.service.UserService

class MainActivity : NavigationActivity() {

    override val menuRes: Int = R.menu.menu_main
    override val layoutRes: Int = R.layout.main_activity
    override val mainPagerRes: Int = R.id.main_pager
    override val bottomNavRes: Int = R.id.bottom_nav

    override val fragmentArguments = listOf(

        /** Home */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_home_base,
            toolbarId = R.id.toolbar_home,
            navHostId = R.id.nav_host_home,
            tabItemId = R.id.tab_home,
            rootDests = rootDestinations),

        /** Diagnose */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_diagnose_base,
            toolbarId = R.id.toolbar_diagnose,
            navHostId = R.id.nav_host_diagnose,
            tabItemId = R.id.tab_diagnose,
            rootDests = rootDestinations),

        /** Report */
        NavigationHostFragment.createArguments(
            layoutRes = R.layout.content_report_base,
            toolbarId = R.id.toolbar_report,
            navHostId = R.id.nav_host_report,
            tabItemId = R.id.tab_report,
            rootDests = rootDestinations))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLoginEventListener()
    }

    private fun setLoginEventListener() {
        val userService: UserService by inject()
        val navigator: Navigator by inject()

        // Finish on logout
        observe(userService.loggedOutEvent()) {
            finish()
        }

        observe(userService.loginNeededEvent()) {
            navigator.showLogin()
        }
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
