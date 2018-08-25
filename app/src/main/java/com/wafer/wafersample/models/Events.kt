package com.wafer.wafersample.models

import com.wafer.wafersample.core.eventmanager.Event

class CountryLoadedEvent(val type: Type, val data: ArrayList<CountryModel>?, val errorMsg: String?, val exception: Throwable?) : Event {
    enum class Type {
        Success, Failure, Error
    }

    constructor(type: Type, data: ArrayList<CountryModel>?) : this(type, data, null, null)
    constructor(type: Type, errorMsg: String?) : this(type, null, errorMsg, null)
    constructor(type: Type, exception: Throwable?) : this(type, null, null, exception)
}