package com.wafer.wafersample.ui.home

import com.wafer.wafersample.models.CountryModel
import com.wafer.wafersample.ui.base.IBaseView

interface IHomeView : IBaseView {

    fun showCountryList(data: ArrayList<CountryModel>)

    fun onItemRemoved(position: Int)

}