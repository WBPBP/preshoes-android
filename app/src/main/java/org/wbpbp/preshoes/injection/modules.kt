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
import org.wbpbp.preshoes.repository.SensorDeviceConnectionRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository
import org.wbpbp.preshoes.service.SensorDeviceService
import org.wbpbp.preshoes.service.SensorDeviceServiceImpl
import org.wbpbp.preshoes.storage.SensorDeviceConnectionRepositoryImpl
import org.wbpbp.preshoes.storage.SensorDeviceStateRepositoryImpl

val myModules = module {

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

    /****************
     * Service
     ****************/
    single {
        SensorDeviceServiceImpl(
            connectionRepo = get(),
            stateRepo = get()
        ) as SensorDeviceService
    }

    /****************
     * Repository
     ****************/
    single {
        SensorDeviceConnectionRepositoryImpl() as SensorDeviceConnectionRepository
    }

    single {
        SensorDeviceStateRepositoryImpl() as SensorDeviceStateRepository
    }
}