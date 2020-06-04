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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.wbpbp.preshoes.R
import org.wbpbp.preshoes.entity.Sample

class FootPressureView(context: Context, private val attrs: AttributeSet)
    : ConstraintLayout(context, attrs) {

    private var available: Boolean = true
    private var side: Int = SIDE_LEFT

    private lateinit var backgroundFoot: ImageView
    private lateinit var heatMap: HeatMap

    init {
        val typedArray = getTypedArray()

        setView(typedArray)

        typedArray.recycle()
    }

    private fun getTypedArray() = context.obtainStyledAttributes(attrs, R.styleable.FootPressureView)

    private fun setView(typedArray: TypedArray) {
        parseAttributes(typedArray)

        addView(inflateView())

        initView()
        renderView()
    }

    private fun parseAttributes(typedArray: TypedArray) {
        side = when (getSide(typedArray)) {
            "left" -> SIDE_LEFT
            "right" -> SIDE_RIGHT
            else -> SIDE_LEFT
        }

        available = typedArray.getBoolean(R.styleable.FootPressureView_available, true)
    }

    private fun getSide(typedArray: TypedArray) =
        typedArray.getString(R.styleable.FootPressureView_side) ?: "left"

    private fun inflateView(): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return inflater.inflate(R.layout.foot_pressure_view, this, false).apply {
            this@FootPressureView.backgroundFoot = findViewById(R.id.background_foot)
            this@FootPressureView.heatMap = findViewById(R.id.heatMap)
        }
    }

    private fun initView() {
        with(heatMap) {
            setMinimum(0.0)
            setMaximum(15.0)

            val start = context.getColor(R.color.colorFootStart)
            val end = context.getColor(R.color.colorFootEnd)

            setColorStops(mapOf(
                0f to start,
                1f to end
            ))
        }
    }

    private fun renderView() {
        setProperFootDrawable()
        setPressureEffectVisibility()
    }

    private fun setProperFootDrawable() {
        with(backgroundFoot) {
            setImageDrawable(context.getDrawable(getFootDrawable()))
        }
    }

    private fun getFootDrawable() = when (side) {
        SIDE_LEFT -> if (available) R.drawable.ic_foot_left else R.drawable.ic_foot_left_disabled
        SIDE_RIGHT -> if (available) R.drawable.ic_foot_right else R.drawable.ic_foot_right_disabled
        else -> if (available) R.drawable.ic_foot_left else R.drawable.ic_foot_left_disabled
    }

    private fun setPressureEffectVisibility() {
        with(heatMap) {
            visibility = if (available) View.VISIBLE else View.GONE
        }
    }

    /**
     * Enable or not.
     * Disabled FootPressureView will have lower alpha and no pressure effects.
     */
    fun setAvailable(available: Boolean) {
        this.available = available

        renderView()
    }

    /**
     * Set pressure values.
     */
    fun setSensorValue(footPressure: Sample?) {
        if (!available) {
            return
        }

        if (footPressure == null) {
            return
        }

        with(heatMap) {
            // forceRefreshOnWorkerThread requires the view to have been drawn.
            // View.post ensures that this requested task executed after layout passed.
            post {
                // Refresh job will be executed on worker thread.
                GlobalScope.launch {
                    clearData()
                    getDataPoints(footPressure).forEach(::addData)

                    try {
                        forceRefreshOnWorkerThread()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // Result will be posted on main thread.
                    MainScope().launch {
                        invalidate()
                    }
                }
            }
        }
    }

    private fun getDataPoints(footPressure: Sample) =
        footPressure.values.mapIndexed { index, value ->
            when(side) {
                SIDE_LEFT -> sensorPointsLeft
                SIDE_RIGHT -> sensorPointsRight
                else -> sensorPointsLeft
            }[index]?.let {
                HeatMap.DataPoint(it.x, it.y, value.toDouble())
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

            5 to SensorPoint(0.26f, 0.5f),
            6 to SensorPoint(0.4f, 0.56f),
            7 to SensorPoint(0.28f, 0.63f),

            8 to SensorPoint(0.35f, 0.77f),
            9 to SensorPoint(0.24f, 0.83f),
            10 to SensorPoint(0.35f, 0.89f),
            11 to SensorPoint(0.46f, 0.83f)
        )

        private val sensorPointsRight = sensorPointsLeft.map {
            Pair(it.key, it.value.copy(x = 1.0f - it.value.x))
        }.toMap()
    }
}