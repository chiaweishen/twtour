package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.databinding.ListItemNearbyContentBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import java.text.DecimalFormat

class HomeNearbyListAdapter : ListAdapter<ScenicSpotInfo, RecyclerView.ViewHolder>(DiffCallback()) {

    private var listener: AdapterListener? = null

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NearbyViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NearbyViewHolder) {
            holder.bindData(getItem(position)) { scenicSpot ->
                listener?.onScenicSpotItemClick(scenicSpot)
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

    class NearbyViewHolder(private val viewBinging: ListItemNearbyContentBinding) :
        RecyclerView.ViewHolder(viewBinging.root) {

        companion object {
            fun newInstance(parent: ViewGroup): NearbyViewHolder {
                return NearbyViewHolder(
                    ListItemNearbyContentBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

        fun bindData(item: ScenicSpotInfo, proceed: (scenicSpot: ScenicSpotInfo) -> Unit) {
            viewBinging.viewPicture.load(item.pictures.firstOrNull())
            viewBinging.textTitle.text = item.name
            viewBinging.textDistance.text = convertDistanceUnit(item.distanceMeter)
            itemView.setOnClickListener {
                proceed.invoke(item)
            }
        }

        private fun convertDistanceUnit(distanceMeter: Int): String {
            return if (distanceMeter >= 1000) {
                DecimalFormat("##0.00").run {
                    "${format(distanceMeter / 1000.0)}km"
                }

            } else {
                "${distanceMeter}m"
            }
        }
    }

    interface AdapterListener {
        fun onScenicSpotItemClick(scenicSpot: ScenicSpotInfo)
    }
}