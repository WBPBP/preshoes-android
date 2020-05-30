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

package org.wbpbp.preshoes.common

import android.app.Application
import android.app.UiModeManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.wbpbp.preshoes.injection.myModules
import org.wbpbp.preshoes.util.Alert
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
        initRealm()
        setDarkMode()
        setFail()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MyApplication)
            modules(myModules)
        }
    }

    private fun initRealm() {
        Realm.init(this)

        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .compactOnLaunch()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        )
    }

    private fun setDarkMode() {
        getSystemService<UiModeManager>()?.nightMode = UiModeManager.MODE_NIGHT_YES

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setFail() {
        Alert.initialize(this)
    }
}