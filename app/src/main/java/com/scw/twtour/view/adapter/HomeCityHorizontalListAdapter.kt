package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.databinding.ListItemCityHorizontalBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.City

class HomeCityHorizontalListAdapter :
    ListAdapter<ScenicSpotInfo, RecyclerView.ViewHolder>(DiffCallback()) {

    private var listener: AdapterListener? = null

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder) {
            holder.bindData(getItem(position)) { city, zipCode ->
                listener?.onCityItemClick(city, zipCode)
            }
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

    class ContentViewHolder(private val viewBinging: ListItemCityHorizontalBinding) :
        RecyclerView.ViewHolder(viewBinging.root) {

        companion object {
            fun newInstance(parent: ViewGroup): ContentViewHolder {
                return ContentViewHolder(
                    ListItemCityHorizontalBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

        fun bindData(item: ScenicSpotInfo, proceed: (city: City?, zipCode: Int?) -> Unit) {
            viewBinging.textTitle.text = item.city?.value
            viewBinging.viewPicture.load(item.pictures.firstOrNull())
            itemView.setOnClickListener {
                proceed.invoke(item.city, item.zipCode)
            }
        }
    }

    interface AdapterListener {
        fun onCityItemClick(city: City?, zipCode: Int?)
    }
}