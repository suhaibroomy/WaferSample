package com.wafer.wafersample.core

import java.util.concurrent.Executor
import android.os.Process
import com.wafer.wafersample.core.executor.MainThreadExecutor
import com.wafer.wafersample.core.executor.PriorityThreadFactory
import com.wafer.wafersample.core.executor.PriorityThreadPoolExecutor
import com.wafer.wafersample.network.HttpManager
import com.wafer.wafersample.network.HttpRunnable
import com.wafer.wafersample.repo.Repository
import com.wafer.wafersample.ui.home.HomePresenter

object CoreComponentsProvider {

    private val DEFAULT_MAX_NUM_THREADS = 2 * Runtime.getRuntime().availableProcessors() + 1
    private val backgroundExecutor: PriorityThreadPoolExecutor
    private val mainThreadExecutor: Executor
    private val httpManager: HttpManager
    private val repository: Repository

    init {
        val backgroundPriorityThreadFactory = PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)
        backgroundExecutor = PriorityThreadPoolExecutor(DEFAULT_MAX_NUM_THREADS, backgroundPriorityThreadFactory)
        mainThreadExecutor = MainThreadExecutor()
        httpManager = HttpManager(backgroundExecutor, mainThreadExecutor)
        repository = Repository(httpManager)
    }

    fun provideHttpRunnable(url: String, priority: Int, listener: HttpRunnable.Listener): HttpRunnable {
        return HttpRunnable(url, priority, listener)
    }

    fun provideHomePresenter(): HomePresenter {
        return HomePresenter(repository)
    }
}