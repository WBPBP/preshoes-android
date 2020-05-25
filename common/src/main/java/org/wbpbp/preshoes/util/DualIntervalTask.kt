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

class MultipleIntervalTaskTimer(
    vararg tasks: Task
) {
    private val _tasks: List<Task> by lazy(tasks::toList)
    private val _lastTaskExecutionTimes = mutableMapOf<Task, Long>()

    private val _timer = Timer()
    private val _timerTask: TimerTask by lazy(::getTimerTask)

    private fun getTimerTask() =
        object: TimerTask() {
            override fun run() {
                _tasks.forEach {
                    if (isItTimeToExecuteTask(it)) {
                        executeTask(it, this)
                    }
                }
            }
        }

    private fun isItTimeToExecuteTask(task: Task): Boolean {
        val lastExecutionTime = _lastTaskExecutionTimes[task] ?: return true
        val now = getCurrentTime()

        return now - lastExecutionTime > task.interval
    }

    private fun executeTask(task: Task, holderTimerTask: TimerTask) {
        task.body(holderTimerTask)

        _lastTaskExecutionTimes[task] = getCurrentTime()
    }

    private fun getCurrentTime() = Date().time

    fun start() {
        _timer.schedule(_timerTask, minInterval)
    }

    fun cancel() {
        _timerTask.cancel()
        _timer.cancel()
    }

    class Task(
        val interval: Long,
        val body: TimerTask.() -> Any?
    )

    companion object {
        private const val minInterval = 10L
    }
}