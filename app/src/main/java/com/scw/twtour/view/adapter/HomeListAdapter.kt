package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.scw.twtour.databinding.ListItemHorizontalContentBinding
import com.scw.twtour.databinding.ListItemTitleBinding
import com.scw.twtour.model.data.MultipleItems
import com.scw.twtour.model.data.ScenicSpotListItem
import com.scw.twtour.model.data.TitleItem

class HomeListAdapter : ListAdapter<ScenicSpotListItem, ViewHolder>(DiffCallback()) {

    enum class ViewType {
        TITLE, CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.TITLE.ordinal -> TitleViewHolder.newInstance(parent)
            ViewType.CONTENT.ordinal -> MultipleContentViewHolder.newInstance(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.bindData(getItem(position) as TitleItem)
            is MultipleContentViewHolder -> holder.bindData(getItem(position) as MultipleItems)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TitleItem -> ViewType.TITLE.ordinal
            is MultipleItems -> ViewType.CONTENT.ordinal
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScenicSpotListItem>() {

        override fun areItemsTheSame(
            oldItem: ScenicSpotListItem,
            newItem: ScenicSpotListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScenicSpotListItem,
            newItem: ScenicSpotListItem
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

class MultipleContentViewHolder(
    viewBinding: ListItemHorizontalContentBinding
) : ViewHolder(viewBinding.root) {

    private val adapter = HomeHorizontalListAdapter()

    init {
        viewBinding.viewRecycler.adapter = adapter
        viewBinding.viewRecycler.layoutManager = LinearLayoutManager(
            itemView.context, RecyclerView.HORIZONTAL, false
        )
    }

    companion object {
        fun newInstance(parent: ViewGroup): MultipleContentViewHolder {
            return MultipleContentViewHolder(
                ListItemHorizontalContentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: MultipleItems) {
        adapter.submitList(item.scenicSpots)
    }
}