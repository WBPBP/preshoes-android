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

package org.wbpbp.preshoes.common.util

import android.graphics.Color

class ColorUtil {
    companion object {
        fun doGradient(
            value: Double,
            min: Double,
            max: Double,
            min_color: Int,
            max_color: Int
        ): Int {
            if (value >= max) {
                return max_color
            }
            if (value <= min) {
                return min_color
            }
            val hsvmin = FloatArray(3)
            val hsvmax = FloatArray(3)
            val frac = ((value - min) / (max - min)).toFloat()
            Color.RGBToHSV(
                Color.red(min_color),
                Color.green(min_color),
                Color.blue(min_color),
                hsvmin
            )
            Color.RGBToHSV(
                Color.red(max_color),
                Color.green(max_color),
                Color.blue(max_color),
                hsvmax
            )
            val retval = FloatArray(3)
            for (i in 0..2) {
                retval[i] = interpolate(hsvmin[i], hsvmax[i], frac)
            }

            return Color.HSVToColor(retval)
        }

        private fun interpolate(
            a: Float,
            b: Float,
            proportion: Float
        ): Float {
            return a + (b - a) * proportion
        }
    }
}