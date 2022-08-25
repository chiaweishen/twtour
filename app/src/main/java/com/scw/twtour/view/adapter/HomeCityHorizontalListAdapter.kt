package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.databinding.ListItemCityHorizontalBinding
import com.scw.twtour.model.data.CityInfo
import com.scw.twtour.constant.City

class HomeCityHorizontalListAdapter :
    ListAdapter<CityInfo, RecyclerView.ViewHolder>(DiffCallback()) {

    private var listener: AdapterListener? = null

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentViewHolder) {
            holder.bindData(getItem(position)) { city ->
                listener?.onCityItemClick(city)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CityInfo>() {

        override fun areItemsTheSame(oldItem: CityInfo, newItem: CityInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CityInfo, newItem: CityInfo): Boolean {
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

        fun bindData(item: CityInfo, proceed: (city: City) -> Unit) {
            viewBinging.textTitle.text = item.city.value
            viewBinging.viewPicture.load(item.drawableResId)
            itemView.setOnClickListener {
                proceed.invoke(item.city)
            }
        }
    }

    interface AdapterListener {
        fun onCityItemClick(city: City)
    }
}