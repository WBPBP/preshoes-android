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

import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.getViewModel(body: T.() -> Unit = {}): T {
    return ViewModelProvider(this).get(T::class.java).apply(body)
}

inline fun <reified T : ViewModel> Fragment.getViewModel(): Lazy<T> {
    return ViewModelLazy(T::class, ::getViewModelStore, ::getDefaultViewModelProviderFactory)
}

fun Fragment.setToolbar(
    @IdRes toolbarId: Int,
    @MenuRes menuId: Int,
    onClick: (MenuItem) -> Boolean = this::onOptionsItemSelected
) {
    activity?.findViewById<Toolbar>(toolbarId)?.let {
        it.inflateMenu(menuId)
        it.setOnMenuItemClickListener(onClick)
    }
}
