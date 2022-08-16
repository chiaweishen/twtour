package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.scw.twtour.databinding.ListItemCityBinding
import com.scw.twtour.databinding.ListItemNearbyBinding
import com.scw.twtour.databinding.ListItemTitleBinding
import com.scw.twtour.model.data.CityItems
import com.scw.twtour.model.data.HomeListItem
import com.scw.twtour.model.data.NearbyItems
import com.scw.twtour.model.data.TitleItem

class HomeListAdapter : ListAdapter<HomeListItem, ViewHolder>(DiffCallback()) {

    enum class ViewType {
        NEARBY, TITLE, CITY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.NEARBY.ordinal -> NearbyViewHolder.newInstance(parent)
            ViewType.TITLE.ordinal -> TitleViewHolder.newInstance(parent)
            ViewType.CITY.ordinal -> CityViewHolder.newInstance(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is NearbyViewHolder -> holder.bindData(getItem(position) as NearbyItems)
            is TitleViewHolder -> holder.bindData(getItem(position) as TitleItem)
            is CityViewHolder -> holder.bindData(getItem(position) as CityItems)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NearbyItems -> ViewType.NEARBY.ordinal
            is TitleItem -> ViewType.TITLE.ordinal
            is CityItems -> ViewType.CITY.ordinal
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

class TitleViewHolder(
    private val viewBinding: ListItemTitleBinding
) : ViewHolder(viewBinding.root) {

    companion object {
        fun newInstance(parent: ViewGroup): TitleViewHolder {
            return TitleViewHolder(
                ListItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: TitleItem) {
        viewBinding.textTitle.text = item.text
    }
}

class CityViewHolder(
    viewBinding: ListItemCityBinding
) : ViewHolder(viewBinding.root) {

    private val adapter = HomeCityHorizontalListAdapter()

    init {
        viewBinding.viewRecycler.adapter = adapter
        viewBinding.viewRecycler.layoutManager = LinearLayoutManager(
            itemView.context, RecyclerView.HORIZONTAL, false
        )
    }

    companion object {
        fun newInstance(parent: ViewGroup): CityViewHolder {
            return CityViewHolder(
                ListItemCityBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: CityItems) {
        adapter.submitList(item.scenicSpots)
    }
}

class NearbyViewHolder(
    viewBinding: ListItemNearbyBinding
) : ViewHolder(viewBinding.root) {

    private val adapter = HomeNearbyHorizontalListAdapter()

    init {
        viewBinding.viewRecycler.adapter = adapter
        viewBinding.viewRecycler.layoutManager = LinearLayoutManager(
            itemView.context, RecyclerView.HORIZONTAL, false
        )
    }

    companion object {
        fun newInstance(parent: ViewGroup): NearbyViewHolder {
            return NearbyViewHolder(
                ListItemNearbyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: NearbyItems) {
        adapter.submitList(item.scenicSpots)
    }
}