package com.scw.twtour.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.databinding.ListItemTitleBinding
import com.scw.twtour.model.data.TitleItem

class TitleViewHolder(
    private val viewBinding: ListItemTitleBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

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