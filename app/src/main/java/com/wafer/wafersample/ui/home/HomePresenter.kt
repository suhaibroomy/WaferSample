package com.wafer.wafersample.ui.home

import com.wafer.wafersample.ui.base.BasePresenter
import com.wafer.wafersample.core.eventmanager.Event
import com.wafer.wafersample.core.eventmanager.EventManager
import com.wafer.wafersample.core.eventmanager.Observer
import com.wafer.wafersample.models.CountryLoadedEvent
import com.wafer.wafersample.models.CountryModel
import com.wafer.wafersample.repo.Repository

class HomePresenter(private val repo: Repository) : BasePresenter<IHomeView>(), IHomePresenter, Observer {

    private val dataList = ArrayList<CountryModel>()

    override fun onAttach(view: IHomeView) {
        super.onAttach(view)
        EventManager.registerObserver(CountryLoadedEvent::class.java, this)
    }

    override fun onViewLoaded() {
        baseView?.showLoading()
        repo.fetchCountries()
    }

    override fun onEvent(event: Event) {
        if (event is CountryLoadedEvent) {
            baseView?.hideLoading()
            when (event.type) {
                CountryLoadedEvent.Type.Success -> {
                    dataList.addAll(event.data!!)
                    baseView?.showCountryList(dataList)
                }
                CountryLoadedEvent.Type.Failure -> {
                    //TODO implement this

                }
                CountryLoadedEvent.Type.Error -> {
                    //TODO implement this
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        EventManager.unregisterObserver(CountryLoadedEvent::class.java, this)
    }

    override fun onItemRemoved(position: Int) {
        dataList.removeAt(position)
        baseView?.onItemRemoved(position)
    }
}