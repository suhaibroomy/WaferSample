package com.wafer.wafersample.network

import com.wafer.wafersample.core.executor.PriorityRunnable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpRunnable(private val url: String, priority: Int, private val listener: Listener) : PriorityRunnable(priority) {

    companion object ErrorCodes {
        const val CANCELLED = -1
        const val IO_ERROR = -2
    }

    interface Listener {
        fun onSuccess(tag: Any, response: String)

        fun onError(tag: Any, responseCode: Int, errorMessage: String)

        fun onFailed(tag: Any, exception: Throwable)
    }

    override fun run() {
        if (Thread.currentThread().isInterrupted or isCancelled) {
            listener.onError(0, CANCELLED, "Cancelled")
            return
        }
        var input: InputStream? = null
        var reader: BufferedReader? = null
        var connection: HttpURLConnection? = null
        try {
            val url = URL(url)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                listener.onError(0, connection.responseCode, connection.responseMessage)
                return
            }

            input = connection.inputStream
            val buffer = StringBuffer()

            if (input == null) {
                listener.onError(0, IO_ERROR, "InputStream was null")
                return
            }

            reader = BufferedReader(InputStreamReader(input))

            var line = reader.readLine()
            while (line != null) {
                buffer.append(line + "\n")
                line = reader.readLine()
            }

            if (buffer.isEmpty()) {
                listener.onError(0, IO_ERROR, "InputStream was null")
            } else {
                listener.onSuccess(0, buffer.toString())
            }


        } catch (e: Exception) {
            listener.onFailed(0, e)
        } finally {
            try {
                reader?.close()
                input?.close()
            } catch (ignored: IOException) {
            }

            connection?.disconnect()
        }
    }
}