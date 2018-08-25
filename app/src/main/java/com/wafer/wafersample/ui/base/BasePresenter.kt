package com.wafer.wafersample.ui.base

abstract class BasePresenter<V : IBaseView> : IBasePresenter<V> {

    var baseView: V? = null

    override fun onAttach(view: V) {
        baseView = view
    }

    override fun onDetach() {
        baseView = null
    }
}