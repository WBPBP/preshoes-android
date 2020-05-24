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

class FakeDataGenerator {
    private var phase: Long = 0

    var state: Int = STATE_STANDING
        set(value) {
            phase = 0
            field = value
        }

    fun getNextFake(channel: Int): ByteArray {
        val phaseToUse = (phase++ % phaseToSectionWeights.size).toInt()

        return if (state == STATE_STANDING) {
            generateStandingFake(phaseToUse)
        } else {
            generateWalkingFake(phaseToUse, channel)
        }
    }

    private fun generateWalkingFake(phase: Int, channel: Int): ByteArray {
        val phaseChannelApplied = (phase + if (channel == CHANNEL_RIGHT) phaseToSectionWeights.size/2 else 0) % phaseToSectionWeights.size
        val sectionWeights = phaseToSectionWeights[phaseChannelApplied]

        val values = Array<Byte>(values) {0}

        sectionWeights.mapIndexed { index, weight ->
            val section = sections[index]

            section.forEach { sensorIndex ->
                 values[sensorIndex] = (pressureMax * weight).toByte()
            }
        }

        return values.toByteArray()
    }

    private fun generateStandingFake(phase: Int): ByteArray {
        return (0..11).map { 12.toByte() }.toByteArray()
    }

    companion object {
        const val CHANNEL_LEFT = 0
        const val CHANNEL_RIGHT = 1

        const val STATE_STANDING = 0
        const val STATE_WALKING = 1

        private const val values = 12
        private const val pressureMax = 15

        private val sections: List<List<Int>> = listOf(
            listOf(8, 9, 10, 11),
            listOf(5, 6, 7),
            listOf(1, 2, 3, 4),
            listOf(0)
        )

        private val phaseToSectionWeights = arrayOf(
            listOf(0f, 0f, 0f, 0f),
            listOf(0f, 0f, 0f, 0f),
            listOf(0f, 0f, 0f, 0f),

            listOf(0f, 0f, 0f, 0f),
            listOf(0.25f, 0f, 0f, 0f),
            listOf(0.5f, 0f, 0f, 0f),
            listOf(0.75f, 0f, 0f, 0f),
            listOf(1f, 0f, 0f, 0f),
            listOf(1f, 0.3f, 0f, 0f),
            listOf(1f, 0.6f, 0f, 0f),
            listOf(1f, 1f, 0f, 0f),
            listOf(0.75f, 1f, 0.5f, 0f),
            listOf(0.5f, 1f, 1f, 0f),

            listOf(0f, 1f, 1f, 0.5f),
            listOf(0f, 0.5f, 1f, 0.75f),
            listOf(0f, 0f, 1f, 1f),
            listOf(0f, 0f, 0.6f, 1f),
            listOf(0f, 0f, 0.3f, 1f),
            listOf(0f, 0f, 0f, 1f),
            listOf(0f, 0f, 0f, 0.75f),
            listOf(0f, 0f, 0f, 0.5f),
            listOf(0f, 0f, 0f, 0.25f),
            listOf(0f, 0f, 0f, 0f),

            listOf(0f, 0f, 0f, 0f),
            listOf(0f, 0f, 0f, 0f),
            listOf(0f, 0f, 0f, 0f)
        )
    }
}