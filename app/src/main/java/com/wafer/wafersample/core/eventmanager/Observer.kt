package com.wafer.wafersample.core.eventmanager

interface Observer {
    fun onEvent(event: Event)
}