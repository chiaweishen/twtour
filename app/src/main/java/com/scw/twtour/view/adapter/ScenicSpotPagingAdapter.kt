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

class ScenicSpotPagingAdapter(private val noteViewSource: NoteViewSource = NoteViewSource.NONE) :
    PagingDataAdapter<ScenicSpotInfo, ScenicSpotPagingAdapter.ScenicSpotViewHolder>(DiffCallback) {

    enum class NoteViewSource {
        NONE, PUSH_PIN, STAR
    }

    private var listener: AdapterListener? = null

    fun setAdapterListener(listener: AdapterListener?) {
        this.listener = listener
    }

    /**
     * https://jermainedilao.medium.com/android-paging-3-library-how-to-update-an-item-in-the-list-52f00d9c99b2
     * FIXME: workaround for updating the item in the paging3 list
     * **/
    fun updateNoteState(scenicSpotInfo: ScenicSpotInfo) {
        for (i in 0 until itemCount) {
            getItem(i)?.also { item ->
                if (item.id == scenicSpotInfo.id) {
                    item.star = scenicSpotInfo.star
                    item.pin = scenicSpotInfo.pin
                    notifyItemChanged(i)
                    return
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ScenicSpotViewHolder, position: Int) {
        holder.bindData(getItem(position), listener, noteViewSource)
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

        fun bindData(
            scenicSpotInfo: ScenicSpotInfo?,
            listener: AdapterListener?,
            noteViewSource: NoteViewSource?
        ) {
            scenicSpotInfo?.also { info ->
                itemView.setOnClickListener {
                    listener?.onItemClick(info)
                }

                if (noteViewSource == NoteViewSource.PUSH_PIN) {
                    viewBinding.viewStar.visibility = View.GONE
                } else if (noteViewSource == NoteViewSource.STAR) {
                    viewBinding.viewPushPin.visibility = View.GONE
                }

                viewBinding.viewStar.setOnClickListener { view ->
                    info.star = !info.star
                    view.isActivated = !view.isActivated
                    listener?.onStarClick(info)
                }
                viewBinding.viewPushPin.setOnClickListener { view ->
                    info.pin = !info.pin
                    view.isActivated = !view.isActivated
                    listener?.onPushPinClick(info)
                }

                viewBinding.viewStar.isActivated = info.star
                viewBinding.viewPushPin.isActivated = info.pin

                viewBinding.viewPicture.load(info.pictures.firstOrNull())
                viewBinding.textTitle.text = info.name

                if (info.description?.isNotBlank() == true) {
                    viewBinding.textDescription.text = info.description
                } else if (info.descriptionDetail?.isNotBlank() == true) {
                    viewBinding.textDescription.text = info.descriptionDetail
                }

                info.city?.also { city ->
                    viewBinding.textCity.text = city.value
                    viewBinding.textCity.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textCity.visibility = View.GONE
                }

                info.zipCodeName?.takeIf { it.isNotBlank() }?.also { zipCodeName ->
                    viewBinding.textZipcode.text = zipCodeName
                    viewBinding.textZipcode.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textZipcode.visibility = View.GONE
                }

                info.classes.firstOrNull()?.also { class1 ->
                    viewBinding.textClass1.text = class1
                    viewBinding.textClass1.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textClass1.visibility = View.GONE
                }

                info.classes.getOrNull(1)?.also { class2 ->
                    viewBinding.textClass2.text = class2
                    viewBinding.textClass2.visibility = View.VISIBLE
                } ?: run {
                    viewBinding.textClass2.visibility = View.GONE
                }

                info.classes.getOrNull(2)?.also { class3 ->
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

    interface AdapterListener {
        fun onItemClick(info: ScenicSpotInfo)
        fun onStarClick(info: ScenicSpotInfo)
        fun onPushPinClick(info: ScenicSpotInfo)
    }
}