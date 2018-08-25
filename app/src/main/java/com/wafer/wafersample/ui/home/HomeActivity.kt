package com.wafer.wafersample.ui.home

import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.wafer.wafersample.models.CountryModel
import com.wafer.wafersample.R
import com.wafer.wafersample.core.CoreComponentsProvider
import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.widget.RecyclerView



class HomeActivity : AppCompatActivity(), IHomeView {
    private val presenter = CoreComponentsProvider.provideHomePresenter()
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter.onAttach(this)

        setupViews()
    }

    private fun setupViews() {
        val swipeController = SwipeController(object: SwipeControllerActions {
            override fun onButtonClicked(position: Int) {
                presenter.onItemRemoved(position)
            }

            override fun onItemSwiped(position: Int) {
                presenter.onItemRemoved(position)
            }

        }, this)

        adapter = CountryAdapter(this, object: CountryAdapter.CountryAdapterDelegate {
            override fun onItemClicked(position: Int) {
                swipeController.reset()
            }

        })
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter


        val touchHelper = ItemTouchHelper(swipeController)
        touchHelper.attachToRecyclerView(recycler_view)
        recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        presenter.onViewLoaded()
    }

    override fun showCountryList(data: ArrayList<CountryModel>) {
        adapter.updateRepoList(data)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun onItemRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}
