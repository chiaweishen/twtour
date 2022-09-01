package com.scw.twtour.view.adapter

import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.scw.twtour.constant.City
import com.scw.twtour.model.data.*
import com.scw.twtour.view.viewholder.CityContentViewHolder
import com.scw.twtour.view.viewholder.DiscoverNearbyViewHolder
import com.scw.twtour.view.viewholder.NearbyViewHolder
import com.scw.twtour.view.viewholder.TitleViewHolder

class HomeListAdapter : ListAdapter<HomeListItem, ViewHolder>(DiffCallback()) {

    private var listener: AdapterListener? = null
    private val lastPositionMap = SparseIntArray()
    private val lastOffsetMap = SparseIntArray()

    enum class ViewType {
        NEARBY, TITLE, CITY, DISCOVER_NEARBY
    }

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.NEARBY.ordinal -> NearbyViewHolder.newInstance(parent, listener)
            ViewType.TITLE.ordinal -> TitleViewHolder.newInstance(parent)
            ViewType.CITY.ordinal -> CityContentViewHolder.newInstance(
                parent,
                lastPositionMap,
                lastOffsetMap,
                listener
            )
            ViewType.DISCOVER_NEARBY.ordinal -> DiscoverNearbyViewHolder.newInstance(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is NearbyViewHolder -> holder.bindData(getItem(position) as NearbyItems)
            is TitleViewHolder -> holder.bindData(getItem(position) as TitleItem)
            is CityContentViewHolder -> holder.bindData(getItem(position) as CityItems)
            is DiscoverNearbyViewHolder -> holder.bindData {
                listener?.onLocationPermissionClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NearbyItems -> ViewType.NEARBY.ordinal
            is TitleItem -> ViewType.TITLE.ordinal
            is CityItems -> ViewType.CITY.ordinal
            is DiscoverNearByItem -> ViewType.DISCOVER_NEARBY.ordinal
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeListItem>() {

        override fun areItemsTheSame(
            oldItem: HomeListItem,
            newItem: HomeListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeListItem,
            newItem: HomeListItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface AdapterListener {
    fun onLocationPermissionClick()
    fun onCityItemClick(city: City)
    fun onScenicSpotItemClick(scenicSpotInfo: ScenicSpotInfo)
}