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

package org.wbpbp.preshoes.util

import java.util.*
import kotlin.concurrent.fixedRateTimer

class MultipleIntervalLimitedTaskTimer(
    private val duration: Long,
    vararg tasks: Task,
    private val onFinish: () -> Any?
) {
    private val _tasks: List<Task> by lazy(tasks::toList)

    private val timers = mutableListOf<Timer>()
    private var startTime: Long = 0

    private fun scheduleTasks() {
        startTime = getCurrentTime()

        val activeTimers = _tasks.map {
            fixedRateTimer(period = it.interval) {
                if (isItTimeToEnd()) {
                    onFinish()
                    cancel()
                } else {
                    it.body(getElapsedTime())
                }
            }
        }

        timers.clear()
        timers.addAll(activeTimers)
    }

    private fun isItTimeToEnd(): Boolean {
        return getElapsedTime() > duration
    }

    private fun getElapsedTime(): Long {
        return getCurrentTime() - startTime
    }

    private fun getCurrentTime() = Date().time

    fun start() {
        scheduleTasks()
    }

    fun cancel() {
        timers.forEach {
            it.cancel()
        }
    }

    class Task(
        val interval: Long,
        val body: (Long) -> Any? /* elapsed */
    )

    companion object {
        private const val minInterval = 10L
    }
}