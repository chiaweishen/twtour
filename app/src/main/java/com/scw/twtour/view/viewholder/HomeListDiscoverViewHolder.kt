package com.scw.twtour.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.databinding.ListItemDiscoverNearbyBinding

class DiscoverNearbyViewHolder(
    private val viewBinding: ListItemDiscoverNearbyBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        fun newInstance(parent: ViewGroup): DiscoverNearbyViewHolder {
            return DiscoverNearbyViewHolder(
                ListItemDiscoverNearbyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(onClickListener: View.OnClickListener) {
        viewBinding.buttonDiscover.setOnClickListener(onClickListener)
    }
}