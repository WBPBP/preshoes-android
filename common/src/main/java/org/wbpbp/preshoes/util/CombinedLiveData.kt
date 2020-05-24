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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class CombinedLiveData<T, K, S>(
    source1: LiveData<T>,
    source2: LiveData<K>,
    private val combine: (data1: T?, data2: K?) -> S
) : MediatorLiveData<S>() {

    private var data1: T? = null
    private var data2: K? = null

    init {
        super.addSource(source1) {
            data1 = it
            value = combine(data1, data2)
        }
        super.addSource(source2) {
            data2 = it
            value = combine(data1, data2)
        }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> removeSource(toRemote: LiveData<T>) {
        throw UnsupportedOperationException()
    }
}