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

package org.wbpbp.preshoes.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.foot_pressure_view_left.view.*
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.extension.resolveThemeColor
import org.wbpbp.preshoes.entity.FootPressure

class FootPressureView(context: Context, private val attrs: AttributeSet)
    : ConstraintLayout(context, attrs) {

    private lateinit var sensors: Array<ImageView>
    private lateinit var foot: ImageView

    init {
        val typedArray = getTypedArray()

        initView(typedArray)
        applyAttrs(typedArray)

        typedArray.recycle()

        scaleSensorDots(IntArray(12) {0})
    }

    private fun getTypedArray() = context.obtainStyledAttributes(attrs, R.styleable.FootPressureView)

    private fun initView(typedArray: TypedArray) {
        val layout = when (getSide(typedArray))  {
            "left" -> R.layout.foot_pressure_view_left
            "right" -> R.layout.foot_pressure_view_right
            else -> R.layout.foot_pressure_view_left
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(layout, this, false)

        addView(view)

        sensors = arrayOf(
            sensor_0, sensor_1, sensor_2, sensor_3,
            sensor_4, sensor_5, sensor_6, sensor_7,
            sensor_8, sensor_9, sensor_10, sensor_11
        )
        foot = background_foot
    }

    private fun getSide(typedArray: TypedArray) = typedArray.getString(R.styleable.FootPressureView_side) ?: "left"

    private fun applyAttrs(typedArray: TypedArray) {
        val footTint = typedArray.getColor(
            R.styleable.FootPressureView_footTint,
            context.resolveThemeColor(R.attr.colorPrimary)
        )
        background_foot.setColorFilter(footTint)

        val sensorTint = typedArray.getColor(
            R.styleable.FootPressureView_sensorTint,
            context.resolveThemeColor(R.attr.colorPrimaryDark)
        )
        sensors.forEach {
            it.setColorFilter(sensorTint)
        }

        val side = typedArray.getString(
            R.styleable.FootPressureView_side
        ) ?: "left"



    }

    fun setSensorValues(footPressure: FootPressure) {
        scaleSensorDots(footPressure.pressures)
    }

    private fun scaleSensorDots(values: IntArray) {
        values.forEachIndexed { index, pressure ->
            sensors[index].scaleX = pressure / 15.0f
            sensors[index].scaleY = pressure / 15.0f
        }
    }
}