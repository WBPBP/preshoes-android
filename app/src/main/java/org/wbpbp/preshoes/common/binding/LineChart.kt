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

package org.wbpbp.preshoes.common.binding

import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.wbpbp.preshoes.common.extension.resolveThemeColor
import timber.log.Timber

@BindingAdapter("chartData")
fun setChartData(view: LineChart, dataSet: List<Double>) {
    val sampleRate = 0.5f

    val leftEntries = dataSet.mapIndexed { index, data ->
        Entry(sampleRate * index, ((1.0 - data) * 100).toFloat())
    }

    val rightEntries = dataSet.mapIndexed { index, data ->
        Entry(sampleRate * index, (data * 100).toFloat())
    }

    val leftDataSet = LineDataSet(leftEntries, "left").apply {
        setValueTextColors(listOf(view.context.resolveThemeColor(android.R.attr.textColorPrimary)))
        valueTextSize = 12f
        color = view.context.resolveThemeColor(android.R.attr.colorPrimary)
    }

    val rightDataSet = LineDataSet(rightEntries, "right").apply {
        setValueTextColors(listOf(view.context.resolveThemeColor(android.R.attr.textColorPrimary)))
        valueTextSize = 12f
        color = view.context.resolveThemeColor(android.R.attr.colorPrimaryDark)
    }

    val dataSets = listOf(leftDataSet, rightDataSet)

    with(view) {
        legend.isEnabled = false

        data = LineData(dataSets)

        xAxis.textColor = 0
        axisLeft.textColor = 0
        axisRight.textColor = 0

        description.textColor = 0
    }

    Timber.i("Chart data set")
}