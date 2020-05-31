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

package org.wbpbp.preshoes.feature.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.koin.android.ext.android.inject
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.repository.UserRepository
import org.wbpbp.preshoes.usecase.Logout

class SettingsFragment : PreferenceFragmentCompat() {
    private val logout: Logout by inject()
    private val userRepo: UserRepository by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        onPreferenceClick("logout") {
            logout(Unit) {
                it
                    .onSuccess { activity?.finish() }
                    .onError { activity?.finish() }
            }
        }

        getPreference("userName")?.summary = userRepo.getUser()?.email
    }

    private fun getPreference(key: String):  Preference? {
        return findPreference<Preference>(key)
    }

    private fun onPreferenceClick(key: String, body: () -> Any?) {
        findPreference<Preference>(key)?.setOnPreferenceClickListener {
            body()
            true
        }
    }
}