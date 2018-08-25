package com.wafer.wafersample.core.executor

abstract class PriorityRunnable(val priority: Int) : Runnable {
    @Volatile var isCancelled = false
}