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
    private var currentPhase = 0

    fun getNextFake() = generateFake((currentPhase++) % phases)

    fun generateFake(phase: Int): ByteArray {
        val sectionWeights = phaseToSectionWeights[phase]

        val values = Array<Byte>(values) {0}

        sectionWeights.mapIndexed { index, weight ->
            val section = sections[index]

            section.forEach { sensorIndex ->
                 values[sensorIndex] = (pressureMax * weight).toByte()
            }
        }

        return values.toByteArray()
    }

    companion object {
        private const val phases = 10
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
            listOf(0.5f, 0f, 0f, 0f),
            listOf(1f, 0f, 0f, 0f),
            listOf(1f, 1f, 0f, 0f),
            listOf(0.5f, 1f, 1f, 0f),

            listOf(0f, 1f, 1f, 0.5f),
            listOf(0f, 0f, 1f, 1f),
            listOf(0f, 0f, 0f, 1f),
            listOf(0f, 0f, 0f, 0.5f),
            listOf(0f, 0f, 0f, 0f)
        )
    }
}