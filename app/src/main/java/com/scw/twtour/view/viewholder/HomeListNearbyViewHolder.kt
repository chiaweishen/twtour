package com.scw.twtour.view.viewholder

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.databinding.ListItemNearbyBinding
import com.scw.twtour.model.data.NearbyItems
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.adapter.AdapterListener
import com.scw.twtour.view.adapter.HomeNearbyListAdapter

class NearbyViewHolder(
    private val viewBinding: ListItemNearbyBinding,
    private val lastPositionMap: SparseIntArray,
    private val lastOffsetMap: SparseIntArray,
    private val listener: AdapterListener?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private val homeNearbyAdapter = HomeNearbyListAdapter()
    private var itemPosition: Int = 0

    init {
        viewBinding.recyclerView.apply {
            this.adapter = homeNearbyAdapter
            layoutManager = LinearLayoutManager(
                itemView.context, RecyclerView.HORIZONTAL, false
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    (layoutManager as? LinearLayoutManager)?.also { manager ->
                        val firstItemPosition = manager.findFirstVisibleItemPosition()
                        val firstChildView = manager.findViewByPosition(firstItemPosition)
                        firstChildView?.apply {
                            lastPositionMap.put(itemPosition, firstItemPosition)
                            lastOffsetMap.put(
                                itemPosition,
                                firstChildView.x.toInt() - recyclerView.paddingRight
                            )
                        }
                    }
                }
            })
        }

        homeNearbyAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        homeNearbyAdapter.setListener(object : HomeNearbyListAdapter.AdapterListener {
            override fun onScenicSpotItemClick(scenicSpot: ScenicSpotInfo) {
                listener?.onScenicSpotItemClick(scenicSpot)
            }
        })
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            lastPositionMap: SparseIntArray,
            lastOffsetMap: SparseIntArray,
            listener: AdapterListener?
        ): NearbyViewHolder {
            return NearbyViewHolder(
                ListItemNearbyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), lastPositionMap, lastOffsetMap, listener
            )
        }
    }

    fun bindData(item: NearbyItems) {
        itemPosition = bindingAdapterPosition
        homeNearbyAdapter.submitList(item.scenicSpots)

        (viewBinding.recyclerView.layoutManager as? LinearLayoutManager)?.also { manager ->
            manager.scrollToPositionWithOffset(
                lastPositionMap.get(itemPosition),
                lastOffsetMap.get(itemPosition)
            )
        }
    }

}