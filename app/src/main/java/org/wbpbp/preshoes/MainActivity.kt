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

package org.wbpbp.preshoes

import org.wbpbp.preshoes.common.base.NavigationActivity
import org.wbpbp.preshoes.common.base.NavigationFragment
import org.wbpbp.preshoes.common.navigation.rootDestinations

class MainActivity : NavigationActivity() {

    override val menuRes: Int = R.menu.menu_main
    override val layoutRes: Int = R.layout.main_activity
    override val mainPagerRes: Int = R.id.main_pager
    override val bottomNavRes: Int = R.id.bottom_nav

    override val fragmentArguments = listOf(

        /** Home */
        NavigationFragment.createArguments(
            layoutRes = R.layout.content_home_base,
            toolbarId = R.id.toolbar_home,
            navHostId = R.id.nav_host_home,
            tabItemId = R.id.tab_home,
            rootDests = rootDestinations),

        /** Diagnose */
        NavigationFragment.createArguments(
            layoutRes = R.layout.content_diagnose_base,
            toolbarId = R.id.toolbar_diagnose,
            navHostId = R.id.nav_host_diagnose,
            tabItemId = R.id.tab_diagnose,
            rootDests = rootDestinations),

        /** Report */
        NavigationFragment.createArguments(
            layoutRes = R.layout.content_report_base,
            toolbarId = R.id.toolbar_report,
            navHostId = R.id.nav_host_report,
            tabItemId = R.id.tab_report,
            rootDests = rootDestinations))
}
