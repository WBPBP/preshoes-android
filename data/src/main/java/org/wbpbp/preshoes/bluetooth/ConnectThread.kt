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

package org.wbpbp.preshoes.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import timber.log.Timber
import java.io.IOException
import java.io.InputStream

/**
 * Heavy duty bluetooth connect & listen thread.
 * All throwable errors are handled, but once an error is thrown, it will stop working.
 */
private class ConnectThread(
    private val device: BluetoothDevice,
    private val onReceive: (ByteArray) -> Any?,
    private val onFail: () -> Any?
) : Thread() {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        SocketCreator(device).createSocket()
    }

    override fun run() {
        cancelDiscovery()

        try {
            startSocketAndNeverReturn() // Only stops on exception
        } catch (e: Exception) {
            Timber.e("Unhandled failure with socket: $e")
        } finally {
            onFail()
            Timber.w("Finishing thread")
        }
    }

    private fun cancelDiscovery() = BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

    private fun startSocketAndNeverReturn() {
        socket?.use {
            it.connect()

            onConnectionSuccess(it)

            Timber.w("Socket is no longer used")
        } ?: run {
            Timber.w("No socket. Nothing to do on thread.")
        }
    }

    private fun onConnectionSuccess(socket: BluetoothSocket) {
        try {
            readForeverAndLaunchCallback(socket.inputStream)
        } catch (e: IOException) {
            Timber.e("Failed to read forever from input stream of connected socket: $e")
        } finally {
            Timber.w("Read loop is over, due to an exception")
        }
    }

    private fun readForeverAndLaunchCallback(stream: InputStream) {
        while (true) {
            readUntilDelimiter(stream, TwelveDProtocol.delimiter).let(::onReceiveData)
        }
    }

    private fun readUntilDelimiter(stream: InputStream, delimiter: Int): ByteArray {
        val bytes: MutableList<Byte> = mutableListOf()
        var input: Int = stream.read()

        while (input != -1) {
            if (input == delimiter) {
                break
            }

            bytes.add(input.toByte())
            input = stream.read()
        }

        return bytes.toByteArray()
    }

    private fun onReceiveData(data: ByteArray) {
        runCallbackOnMainThread(data)
    }

    private fun runCallbackOnMainThread(data: ByteArray) {
        mainHandler.post {
            try {
                onReceive(data)
            } catch (e: Exception) {
                Timber.e("Error inside onReceive callback: $e")
            }
        }
    }

    /**
     * Closes the client socket and causes the thread to finish.
     * MUST be called when thread is terminated from outside.
     */
    fun cancel() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Timber.e("Could not close the client socket: $e")
        }
    }
}