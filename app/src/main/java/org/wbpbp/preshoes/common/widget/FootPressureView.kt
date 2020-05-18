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
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import ca.hss.heatmaplib.HeatMap
import ca.hss.heatmaplib.HeatMapMarkerCallback.CircleHeatMapMarker
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.common.util.ColorUtil
import org.wbpbp.preshoes.entity.FootPressure

class FootPressureView(context: Context, private val attrs: AttributeSet)
    : ConstraintLayout(context, attrs) {

    private var _enabled: Boolean = true
    private var _side: Int = SIDE_LEFT
    private var _footDrawableId: Int = -1

    private lateinit var heatMap: HeatMap

    init {
        val typedArray = getTypedArray()

        setView(typedArray)

        typedArray.recycle()
    }

    private fun getTypedArray() = context.obtainStyledAttributes(attrs, R.styleable.FootPressureView)

    private fun setView(typedArray: TypedArray) {
        val view = inflateView()

        parseAttributes(typedArray)
        initView(view)
        addView(view)
    }

    private fun parseAttributes(typedArray: TypedArray) {
        _enabled = typedArray.getBoolean(R.styleable.FootPressureView_enabled, true)

        _side = when (getSide(typedArray)) {
            "left" -> SIDE_LEFT
            "right" -> SIDE_RIGHT
            else -> SIDE_LEFT
        }

        _footDrawableId = when (_side) {
            SIDE_LEFT -> if (_enabled) R.drawable.ic_foot_left else R.drawable.ic_foot_left_disabled
            SIDE_RIGHT -> if (_enabled) R.drawable.ic_foot_right else R.drawable.ic_foot_right_disabled
            else -> if (_enabled) R.drawable.ic_foot_left else R.drawable.ic_foot_left_disabled
        }
    }

    private fun getSide(typedArray: TypedArray) =
        typedArray.getString(R.styleable.FootPressureView_side) ?: "left"

    private fun inflateView(): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return inflater.inflate(R.layout.foot_pressure_view, this, false)
    }

    private fun initView(root: View) {
        with(root.findViewById<ImageView>(R.id.background_foot)) {
            setImageDrawable(context.getDrawable(_footDrawableId))
        }

        initViewChildren(root)
    }

    private fun initViewChildren(root: View) {
        heatMap = root.findViewById(R.id.heatMap)
        with(heatMap) {
            setMinimum(0.0)
            setMaximum(100.0)
            setRadius(500.0)
            setMarkerCallback(CircleHeatMapMarker(-0x6bff2d))

            val colors = (0..20).map {
                Pair(it.toFloat() / 20.0f,
                    ColorUtil.doGradient(
                        it * 5.toDouble(),
                        0.0,
                        100.0,
                        -0x00ff00,
                        -0x009900))
            }.toMap()

            setColorStops(colors)
        }
    }

    fun setSensorValues(footPressure: FootPressure) {
        with(heatMap) {
            clearData()
            getDataPoints(footPressure).forEach(::addData)
            forceRefresh()
        }
    }

    private fun getDataPoints(footPressure: FootPressure) =
        footPressure.values.mapIndexed { index, value ->
            when(_side) {
                SIDE_LEFT -> sensorPointsLeft
                SIDE_RIGHT -> sensorPointsRight
                else -> sensorPointsLeft
            }[index]?.let {
                HeatMap.DataPoint(it.x, it.y, (value / 15.toDouble()) * 100)
            }
        }.filterNotNull().toTypedArray()

    data class SensorPoint(val x: Float, val y: Float)

    companion object {
        private const val SIDE_LEFT = 0
        private const val SIDE_RIGHT = 1

        private val sensorPointsLeft = mapOf(
            0 to SensorPoint(0.83f, 0.1f),
            1 to SensorPoint(0.78f, 0.25f),
            2 to SensorPoint(0.6f, 0.26f),
            3 to SensorPoint(0.42f, 0.3f),
            4 to SensorPoint(0.26f, 0.37f),
            5 to SensorPoint(0.4f, 0.56f),
            6 to SensorPoint(0.26f, 0.5f),
            7 to SensorPoint(0.28f, 0.63f),
            8 to SensorPoint(0.35f, 0.77f),
            9 to SensorPoint(0.46f, 0.83f),
            10 to SensorPoint(0.24f, 0.83f),
            11 to SensorPoint(0.35f, 0.89f)
        )

        private val sensorPointsRight = sensorPointsLeft.map {
            Pair(it.key, it.value.copy(x = 1.0f - it.value.x))
        }.toMap()
    }
}