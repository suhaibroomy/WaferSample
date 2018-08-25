package com.wafer.wafersample.ui.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wafer.wafersample.models.CountryModel
import com.wafer.wafersample.R

class CountryAdapter(context: Context, private val delegate: CountryAdapterDelegate) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    interface CountryAdapterDelegate {
        fun onItemClicked(position: Int)
    }

    private lateinit var countryList: List<CountryModel>
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(layoutInflater.inflate(R.layout.item_country, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return if (::countryList.isInitialized) countryList.size else 0
    }

    override fun onBindViewHolder(viewHolder: CountryViewHolder, position: Int) {
        viewHolder.onBind(countryList[position])
        viewHolder.itemView.setOnClickListener {
            delegate.onItemClicked(position)
        }
    }

    fun updateRepoList(countryList: List<CountryModel>) {
        this.countryList = countryList
        notifyDataSetChanged()
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val tvLanguage = itemView.findViewById<TextView>(R.id.tv_language)
        private val tvCurrency = itemView.findViewById<TextView>(R.id.tv_currency)

        fun onBind(countryModel: CountryModel) {
            tvName.text = countryModel.name
            tvLanguage.text = tvLanguage.context.getString(R.string.language, countryModel.getLanguage())
            tvCurrency.text = tvCurrency.context.getString(R.string.currency, countryModel.getCurrency())
        }
    }

    override fun onViewRecycled(holder: CountryViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.x = 0f
    }
}