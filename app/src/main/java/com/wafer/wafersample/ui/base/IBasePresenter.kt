package com.wafer.wafersample.ui.base

interface IBasePresenter<V : IBaseView> {

    fun onAttach(view: V)

    fun onDetach()

    fun onViewLoaded()
}