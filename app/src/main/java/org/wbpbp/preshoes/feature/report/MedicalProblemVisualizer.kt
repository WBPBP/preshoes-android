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

package org.wbpbp.preshoes.feature.report

import android.content.Context
import android.graphics.drawable.Drawable
import org.wbpbp.preshoes.R

object MedicalProblemVisualizer {

    fun getDrawableOfPossibleMedicalProblem(context: Context, medicalProblemId: Int): Drawable? {
        return context.getDrawable(
            when (medicalProblemId) {
                1 -> R.drawable.ic_disease_1
                2 -> R.drawable.ic_disease_2
                3 -> R.drawable.ic_disease_3
                4 -> R.drawable.ic_disease_4
                else -> R.drawable.ic_disease_0
            }
        )
    }

    fun getNameOfPossibleMedicalProblem(context: Context, medicalProblemId: Int): String {
        return context.getString(
            when (medicalProblemId) {
                1 -> R.string.disease_name_1
                2 -> R.string.disease_name_2
                3 -> R.string.disease_name_3
                4 -> R.string.disease_name_4
                else -> R.string.disease_name_0
            })
    }
}