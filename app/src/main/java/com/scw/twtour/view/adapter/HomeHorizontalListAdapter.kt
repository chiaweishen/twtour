package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.databinding.ListHorizontalItemBinding
import com.scw.twtour.model.data.ScenicSpotInfo

class HomeHorizontalListAdapter :
    ListAdapter<ScenicSpotInfo, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder) {
            holder.bindData(getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScenicSpotInfo>() {

        override fun areItemsTheSame(oldItem: ScenicSpotInfo, newItem: ScenicSpotInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScenicSpotInfo, newItem: ScenicSpotInfo): Boolean {
            return oldItem == newItem
        }
    }

    class ContentViewHolder(private val viewBinging: ListHorizontalItemBinding) :
        RecyclerView.ViewHolder(viewBinging.root) {

        companion object {
            fun newInstance(parent: ViewGroup): ContentViewHolder {
                return ContentViewHolder(
                    ListHorizontalItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

        fun bindData(item: ScenicSpotInfo) {
            viewBinging.textTitle.text = item.city
            viewBinging.viewPicture.load(item.pictures.firstOrNull())
        }
    }
}