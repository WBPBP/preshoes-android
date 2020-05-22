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

package org.wbpbp.preshoes.service

import org.wbpbp.preshoes.entity.Sample
import org.wbpbp.preshoes.repository.SensorDeviceConnectionRepository
import org.wbpbp.preshoes.repository.SensorDeviceStateRepository

class SensorDeviceServiceImpl(
    private val deviceConnectionRepo: SensorDeviceConnectionRepository,
    private val deviceStateRepo: SensorDeviceStateRepository
) : SensorDeviceService {

    // TODO remove these all after test
    private var base: Int = 0

    override fun enterRandomState() {
        with(deviceStateRepo) {
            isRightDeviceConnected.postValue(true)
            isLeftDeviceConnected.postValue(/*base % 200 > 100*/true)

            leftDeviceBatteryLevel.postValue(45)
            rightDeviceBatteryLevel.postValue(67)

            isLeftDeviceCharging.postValue(false)
            isRightDeviceCharging.postValue(true)

            leftDeviceSensorValue.postValue(getRandomFootPressureValue())
            rightDeviceSensorValue.postValue(getRandomFootPressureValue())
        }
    }

    private fun getRandomFootPressureValue(): Sample {
        return Sample(IntArray(12) {base%16}).also {
            base++
        }
    }
}