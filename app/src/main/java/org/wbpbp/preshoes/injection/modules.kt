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
import org.wbpbp.preshoes.common.navigation.Navigator
import org.wbpbp.preshoes.helper.BluetoothHelper
import org.wbpbp.preshoes.helper.BluetoothHelperImpl
import org.wbpbp.preshoes.repository.SampleRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.repository.SystemStateRepository
import org.wbpbp.preshoes.service.*
import org.wbpbp.preshoes.storage.SampleRepositoryImpl
import org.wbpbp.preshoes.storage.SensorDeviceStateRepositoryImpl
import org.wbpbp.preshoes.storage.SystemStateRepositoryImpl
import org.wbpbp.preshoes.usecase.ConnectDevices
import org.wbpbp.preshoes.usecase.CreateReport
import org.wbpbp.preshoes.usecase.FinishRecording
import org.wbpbp.preshoes.usecase.StartRecording

val myModules = module {

    /**
     * TODO
     */
    single {
        FakeDataGenerator()
    }

    /****************
     * Common
     ****************/
    single {
        Navigator(
            context = get()
        )
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
        FinishRecording(
            service = get()
        )
    }

    single {
        StartRecording(
            service = get()
        )
    }

    /****************
     * Helper
     ****************/
    single {
        BluetoothHelperImpl() as BluetoothHelper
    }

    /****************
     * Service
     ****************/
    single {
        RecordServiceImpl(
            sampleRepo = get(),
            recordRepo = get()
        ) as RecordService
    }

    single {
        SensorDeviceServiceImpl(
            deviceStateRepo = get(),
            bluetoothHelper = get()
        ) as SensorDeviceService
    }

    /****************
     * Repository
     ****************/
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
}