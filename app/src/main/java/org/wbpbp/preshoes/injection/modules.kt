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

package org.wbpbp.preshoes.injection

import org.koin.dsl.module
import org.wbpbp.preshoes.BuildConfig
import org.wbpbp.preshoes.bluetooth.BluetoothHelper
import org.wbpbp.preshoes.bluetooth.BluetoothHelperImpl
import org.wbpbp.preshoes.bluetooth.BluetoothHelperTestImpl
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.entity.Config
import org.wbpbp.preshoes.repository.*
import org.wbpbp.preshoes.service.*
import org.wbpbp.preshoes.storage.*
import org.wbpbp.preshoes.usecase.*

val myModules = module {

    /****************
     * Common
     ****************/
    single {
        Config()
    }

    single {
        Navigator(
            context = get()
        )
    }

    single {
        RetrofitFactory.createPreshoesNetworkService(
            context = get()
        ) as ApiService
    }

    /****************
     * Use Case
     ****************/
    single {
        ConnectDevices(
            service = get()
        )
    }

    single {
        CreateReport(
            service = get()
        )
    }

    single {
        DeleteReport(
            repo = get()
        )
    }

    single {
        FinishStandingRecording(
            service = get()
        )
    }

    single {
        FinishWalkingRecording(
            service = get()
        )
    }

    single {
        GetReports(
            repo = get()
        )
    }

    single {
        GetUser(
            repo = get()
        )
    }

    single {
        HaltRecording(
            service = get()
        )
    }

    single {
        SignIn(
            service = get()
        )
    }

    single {
        SignUp(
            service = get()
        )
    }

    single {
        StartStandingRecording(
            service = get()
        )
    }

    single {
        StartWalkingRecording(
            service = get()
        )
    }

    single {
        SyncReport(
            service = get()
        )
    }

    /****************
     * Bluetooth
     ****************/
    single {
        when (BuildConfig.FLAVOR_bluetooth) {
            "realDevice" -> BluetoothHelperImpl()
            "fakeDevice" -> BluetoothHelperTestImpl()
            else -> BluetoothHelperImpl()
        } as BluetoothHelper
    }

    /****************
     * Service
     ****************/
    single {
        ReportServiceImpl(
            sampleRepo = get(),
            reportRepo = get()
        ) as ReportService
    }

    single {
        SensorDeviceServiceImpl(
            deviceStateRepo = get(),
            bluetoothHelper = get()
        ) as SensorDeviceService
    }

    single {
        val deployImpl = UserServiceImpl(
            api = get(),
            userRepo = get()
        )

        when (BuildConfig.FLAVOR_server) {
            "deployServer" -> deployImpl
            "mockServer" -> UserServiceTestImpl()
            else -> deployImpl
        } as UserService
    }

    /****************
     * Repository
     ****************/
    single {
        ReportRepositoryImpl(
            api = get()
        ) as ReportRepository
    }

    single {
        SampleRepositoryImpl(
            sensorDeviceStateRepo = get()
        ) as SampleRepository
    }

    single {
        SensorDeviceStateRepositoryImpl() as SensorDeviceStateRepository
    }

    single {
        SystemStateRepositoryImpl() as SystemStateRepository
    }

    single {
        UserRepositoryImpl() as UserRepository
    }
}