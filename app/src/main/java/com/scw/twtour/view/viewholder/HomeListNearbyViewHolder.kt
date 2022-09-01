package com.scw.twtour.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.R
import com.scw.twtour.databinding.ListItemNearbyBinding
import com.scw.twtour.model.data.NearbyItems
import com.scw.twtour.view.adapter.AdapterListener
import java.text.DecimalFormat

class NearbyViewHolder(
    private val viewBinding: ListItemNearbyBinding,
    private val listener: AdapterListener?
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        fun newInstance(parent: ViewGroup, listener: AdapterListener?): NearbyViewHolder {
            return NearbyViewHolder(
                ListItemNearbyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), listener
            )
        }
    }

    fun bindData(item: NearbyItems) {
        viewBinding.carouselView.apply {
            size = item.scenicSpots.size
            hideIndicator(true)
            setCarouselViewListener { view, position ->
                val imageView = view.findViewById<ImageView>(R.id.view_picture)
                val textTitle = view.findViewById<TextView>(R.id.text_title)
                val textDistance = view.findViewById<TextView>(R.id.text_distance)

                item.scenicSpots[position].apply {
                    view.setOnClickListener {
                        listener?.onScenicSpotItemClick(this)
                    }
                    imageView.load(pictures.firstOrNull())
                    textTitle.text = name
                    textDistance.text = convertDistanceUnit(distanceMeter)
                }
            }
            show()
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