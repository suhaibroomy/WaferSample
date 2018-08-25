package com.wafer.wafersample.core.executor

import java.util.concurrent.FutureTask

class PriorityFutureTask(private val runnable: PriorityRunnable) :
        FutureTask<PriorityRunnable>(runnable, null), Comparable<PriorityFutureTask> {

    override fun compareTo(other: PriorityFutureTask): Int {
        val p1 = runnable.priority
        val p2 = other.runnable.priority
        return p2 - p1
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        runnable.isCancelled = true
        return super.cancel(mayInterruptIfRunning)
    }
}