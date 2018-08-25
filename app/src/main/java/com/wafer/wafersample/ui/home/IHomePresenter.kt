package com.wafer.wafersample.ui.home

import com.wafer.wafersample.ui.base.IBasePresenter

interface IHomePresenter : IBasePresenter<IHomeView> {
    fun onItemRemoved(position: Int)
}