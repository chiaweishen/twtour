package com.scw.twtour.view.util

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.ScreenUtil
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
import timber.log.Timber

class ScenicSpotPagingLayoutManager(
    private val recyclerView: RecyclerView,
    private val fab: FloatingActionButton,
    private val pagingAdapter: ScenicSpotPagingAdapter,
    private val textEmpty: TextView,
    private val progressIndicator: LinearProgressIndicator
) {
    private val context = recyclerView.context
    private var listener: AdapterListener? = null

    fun initView(listener: AdapterListener) {
        this.listener = listener
        initRecyclerView()
        initFloatingActionButton()
    }

    private fun initRecyclerView() {
        recyclerView.adapter = pagingAdapter

        pagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        pagingAdapter.setAdapterListener(object : ScenicSpotPagingAdapter.AdapterListener {
            override fun onItemClick(info: ScenicSpotInfo) {
                listener?.onItemClick(info)
            }

            override fun onStarClick(info: ScenicSpotInfo) {
                listener?.onStarClick(info)
            }

            override fun onPushPinClick(info: ScenicSpotInfo) {
                listener?.onPushPinClick(info)
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offsetVertical = recyclerView.computeVerticalScrollOffset()
                val screenHeight = ScreenUtil.getScreenHeight(context)
                updateFloatingActionButton(dy < 0 && offsetVertical > screenHeight)
            }
        })
    }

    private fun initFloatingActionButton() {
        fab.setOnClickListener {
            val offsetVertical = recyclerView.computeVerticalScrollOffset()
            val screenHeight = ScreenUtil.getScreenHeight(context)
            if (offsetVertical > screenHeight * 5) {
                recyclerView.scrollToPosition(0)
            } else {
                recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private fun updateFloatingActionButton(show: Boolean) {
        if (show) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    fun updateLoadingState(loadStates: CombinedLoadStates) {
        Timber.i("Paging load states: $loadStates")
        if (loadStates.source.refresh is LoadState.NotLoading &&
            loadStates.append.endOfPaginationReached &&
            pagingAdapter.itemCount < 1
        ) {
            textEmpty.visibility = View.VISIBLE
        } else {
            textEmpty.visibility = View.GONE
        }

        progressIndicator.visibility =
            if (loadStates.append is LoadState.Loading) View.VISIBLE else View.GONE

        val errorState = when {
            loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
            loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
            loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
            else -> null
        }
        errorState?.also {
            Timber.e(Log.getStackTraceString(it.error))
        }
    }

    interface AdapterListener {
        fun onItemClick(info: ScenicSpotInfo)
        fun onStarClick(info: ScenicSpotInfo)
        fun onPushPinClick(info: ScenicSpotInfo)
    }
}