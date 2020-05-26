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

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.wbpbp.preshoes.base.Failure
import org.wbpbp.preshoes.extension.has
import timber.log.Timber

object Alert {
    private const val DEBUG = 0x01
    private const val USUAL = 0x02 // default
    private const val WTF = 0x04

    private const val level = DEBUG

    private var context: Context? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val allChannels = mapOf<Int, MutableLiveData<Failure>>(
        Pair(DEBUG, MutableLiveData()),
        Pair(USUAL, MutableLiveData()),
        Pair(WTF, MutableLiveData()))

    private val availableChannels = allChannels.filter { it.key >= level }

    fun initialize(context: Context) {
        this.context = context
    }

    fun debug(@StringRes failure: Int) = debug(this.context!!.getString(failure))
    fun debug(failure: String) = debug(Failure(failure), true)
    fun debug(failure: Failure, verbose: Boolean = true) {
        emit(failure, DEBUG, verbose)
    }

    fun usual(@StringRes failure: Int) = usual(this.context!!.getString(failure))
    fun usual(failure: String) = usual(Failure(failure), true)
    fun usual(failure: Failure, verbose: Boolean = true) {
        emit(failure, USUAL, verbose)
    }

    fun wtf(@StringRes failure: Int) = wtf(this.context!!.getString(failure))
    fun wtf(failure: String) = wtf(Failure(failure), true)
    fun wtf(failure: Failure, verbose: Boolean = true) {
        emit(failure, WTF, verbose)
    }

    private fun emit(failure: Failure, channel: Int, verbose: Boolean) {
        if (verbose) {
            toast(failure.message)
        }

        emitEvent(failure, channel)
    }

    private fun toast(message: String) {
        context?.let {
            mainHandler.post {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun emitEvent(failure: Failure, channel: Int) {
        availableChannels[channel]?.let { channelFound ->
            Timber.d("Failure set in channel $channel: $failure")

            channelFound.postValue(failure)
        }
    }

    fun observe(lifecycleOwner: LifecycleOwner,
                channel: Int = DEBUG.or(USUAL).or(WTF),
                body: (Failure, Int) -> Any?) {

        getChannelsToObserve(channel).forEach { entry ->
            Timber.d( "$lifecycleOwner listening on ${entry.key} channel")

            entry.value.observe(lifecycleOwner, Observer { failure ->
                body(failure, entry.key)
            })
        }
    }

    private fun getChannelsToObserve(channel: Int): Map<Int, MutableLiveData<Failure>> {
        return availableChannels.filter { channel.has(it.key) }
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        availableChannels.forEach { entry ->
            Timber.d("$lifecycleOwner removing observers on ${entry.key}")

            entry.value.removeObservers(lifecycleOwner)
        }
    }
}