package com.wafer.wafersample.core.executor

import com.wafer.wafersample.core.executor.PriorityFutureTask
import com.wafer.wafersample.core.executor.PriorityRunnable
import java.util.concurrent.*

class PriorityThreadPoolExecutor internal constructor(maxNumThreads: Int, threadFactory: ThreadFactory) :
        ThreadPoolExecutor(maxNumThreads, maxNumThreads, 0, TimeUnit.MILLISECONDS, PriorityBlockingQueue(), threadFactory) {

    override fun submit(task: Runnable): PriorityFutureTask {
        val futureTask = PriorityFutureTask(task as PriorityRunnable)
        execute(futureTask)
        return futureTask
    }
}