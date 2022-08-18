package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.twtour.databinding.ListItemScenicSpotBinding
import com.scw.twtour.model.data.ScenicSpotInfo

class ScenicSpotPagingAdapter :
    PagingDataAdapter<ScenicSpotInfo, ScenicSpotPagingAdapter.ScenicSpotViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ScenicSpotViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScenicSpotViewHolder {
        return ScenicSpotViewHolder.newInstance(parent)
    }

    class ScenicSpotViewHolder(
        private val viewBinding: ListItemScenicSpotBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        companion object {
            fun newInstance(parent: ViewGroup): ScenicSpotViewHolder {
                val binding = ListItemScenicSpotBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return ScenicSpotViewHolder(binding)
            }
        }

        fun bindData(info: ScenicSpotInfo?) {
            info?.also {
                viewBinding.viewPicture.load(it.pictures.firstOrNull())
                viewBinding.textTitle.text = it.name

                if (it.description?.isNotBlank() == true) {
                    viewBinding.textDescription.text = it.description
                } else if (it.descriptionDetail?.isNotBlank() == true) {
                    viewBinding.textDescription.text = it.descriptionDetail
                }

                it.classes.firstOrNull()?.also { class1 ->
                    viewBinding.textClass1.text = class1
                    viewBinding.textClass1.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textClass1.visibility = View.GONE
                }

                it.classes.getOrNull(1)?.also { class2 ->
                    viewBinding.textClass2.text = class2
                    viewBinding.textClass2.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textClass2.visibility = View.GONE
                }

                it.classes.getOrNull(2)?.also { class3 ->
                    viewBinding.textClass3.text = class3
                    viewBinding.textClass3.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textClass3.visibility = View.GONE
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<ScenicSpotInfo>() {

        override fun areItemsTheSame(oldItem: ScenicSpotInfo, newItem: ScenicSpotInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScenicSpotInfo, newItem: ScenicSpotInfo): Boolean {
            return oldItem == newItem
        }
    }
}