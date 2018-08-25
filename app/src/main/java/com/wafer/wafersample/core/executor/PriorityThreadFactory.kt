package com.wafer.wafersample.core.executor

import java.util.concurrent.ThreadFactory
import android.os.Process

class PriorityThreadFactory internal constructor(private val mThreadPriority: Int) : ThreadFactory {

    override fun newThread(runnable: Runnable): Thread {
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(mThreadPriority)
            } catch (ignored: Throwable) {

            }

            runnable.run()
        }
        return Thread(wrapperRunnable)
    }
}