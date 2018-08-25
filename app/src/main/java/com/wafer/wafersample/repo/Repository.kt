package com.wafer.wafersample.repo

import com.google.gson.reflect.TypeToken
import com.wafer.wafersample.utils.objectify
import com.wafer.wafersample.core.eventmanager.EventManager
import com.wafer.wafersample.models.CountryModel
import com.wafer.wafersample.models.CountryLoadedEvent
import com.wafer.wafersample.network.HIGH_PRIORITY
import com.wafer.wafersample.network.HttpManager
import com.wafer.wafersample.network.HttpRunnable

private const val COUNTRY_APPEND_URL = "rest/v2/all"

class Repository(private val httpManager: HttpManager) {

    val COUNTRY_MODEL_ARRAY_LIST_TYPE = object : TypeToken<ArrayList<CountryModel>>() {}.type!!

    fun fetchCountries() {
        httpManager.get(COUNTRY_APPEND_URL, HIGH_PRIORITY, object : HttpRunnable.Listener {
            override fun onSuccess(tag: Any, response: String) {
                response.objectify<ArrayList<CountryModel>>(COUNTRY_MODEL_ARRAY_LIST_TYPE)?.let {
                    EventManager.sendEvent(CountryLoadedEvent(CountryLoadedEvent.Type.Success, data = it))
                } ?: run {
                    EventManager.sendEvent(CountryLoadedEvent(CountryLoadedEvent.Type.Error, errorMsg = "Could not parse JSON"))
                }

            }

            override fun onError(tag: Any, responseCode: Int, errorMessage: String) {
                EventManager.sendEvent(CountryLoadedEvent(CountryLoadedEvent.Type.Error, errorMsg = errorMessage))
            }

            override fun onFailed(tag: Any, exception: Throwable) {
                EventManager.sendEvent(CountryLoadedEvent(CountryLoadedEvent.Type.Error, exception = exception))
            }

        }, COUNTRY_APPEND_URL)
    }
}