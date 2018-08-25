package com.wafer.wafersample.network

import com.wafer.wafersample.core.CoreComponentsProvider
import com.wafer.wafersample.core.executor.MainThreadExecutor
import com.wafer.wafersample.core.executor.PriorityFutureTask
import com.wafer.wafersample.core.executor.PriorityThreadPoolExecutor

private const val BASE_URL = "https://restcountries.eu/"
const val HIGH_PRIORITY = Integer.MAX_VALUE

class HttpManager(private val backgroundThreadExecutor: PriorityThreadPoolExecutor, private val mainThreadExecutor: MainThreadExecutor) {

    private var currentPriority = 1
    private val requestMap = HashMap<String, PriorityFutureTask>()

    fun get(appendUrl: String, priority: Int, listener: HttpRunnable.Listener, requestTag: Any) {

        val fullUrl = BASE_URL + appendUrl

        if (requestMap.containsKey(fullUrl)) {
            requestMap[fullUrl]?.cancel(false)
        }

        val runnable = CoreComponentsProvider.provideHttpRunnable(fullUrl, priority, object: HttpRunnable.Listener {
            override fun onSuccess(tag: Any, response: String) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onSuccess(requestTag, response)
                }
            }

            override fun onError(tag: Any, responseCode: Int, errorMessage: String) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onError(requestTag, responseCode, errorMessage)
                }
            }

            override fun onFailed(tag: Any, exception: Throwable) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onFailed(requestTag, exception)
                }
            }

        })

        val future = backgroundThreadExecutor.submit(runnable)
        requestMap[fullUrl] = future
    }

    fun get(appendUrl: String, listener: HttpRunnable.Listener, requestTag: Any) {

        val fullUrl = BASE_URL + appendUrl

        if (requestMap.containsKey(fullUrl)) {
            requestMap[fullUrl]?.cancel(false)
        }

        val runnable = CoreComponentsProvider.provideHttpRunnable(fullUrl, currentPriority, object: HttpRunnable.Listener {
            override fun onSuccess(tag: Any, response: String) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onSuccess(requestTag, response)
                }
            }

            override fun onError(tag: Any, responseCode: Int, errorMessage: String) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onError(requestTag, responseCode, errorMessage)
                }
            }

            override fun onFailed(tag: Any, exception: Throwable) {
                mainThreadExecutor.execute {
                    requestMap.remove(fullUrl)
                    listener.onFailed(requestTag, exception)
                }
            }

        })

        val future = backgroundThreadExecutor.submit(runnable)
        requestMap[fullUrl] = future
        currentPriority += 1
    }
}