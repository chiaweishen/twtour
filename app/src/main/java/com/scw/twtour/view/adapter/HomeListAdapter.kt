package com.scw.twtour.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.scw.twtour.R
import com.scw.twtour.constant.City
import com.scw.twtour.databinding.ListItemCityBinding
import com.scw.twtour.databinding.ListItemDiscoverNearbyBinding
import com.scw.twtour.databinding.ListItemNearbyBinding
import com.scw.twtour.databinding.ListItemTitleBinding
import com.scw.twtour.model.data.*
import java.text.DecimalFormat

class HomeListAdapter : ListAdapter<HomeListItem, ViewHolder>(DiffCallback()) {

    private var listener: AdapterListener? = null

    enum class ViewType {
        NEARBY, TITLE, CITY, DISCOVER_NEARBY
    }

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.NEARBY.ordinal -> NearbyViewHolder.newInstance(parent)
            ViewType.TITLE.ordinal -> TitleViewHolder.newInstance(parent)
            ViewType.CITY.ordinal -> CityContentViewHolder.newInstance(parent)
            ViewType.DISCOVER_NEARBY.ordinal -> DiscoverNearbyViewHolder.newInstance(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is NearbyViewHolder -> holder.bindData(getItem(position) as NearbyItems, listener)
            is TitleViewHolder -> holder.bindData(getItem(position) as TitleItem)
            is CityContentViewHolder -> holder.bindData(getItem(position) as CityItems, listener)
            is DiscoverNearbyViewHolder -> holder.bindData {
                listener?.onLocationPermissionClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NearbyItems -> ViewType.NEARBY.ordinal
            is TitleItem -> ViewType.TITLE.ordinal
            is CityItems -> ViewType.CITY.ordinal
            is DiscoverNearByItem -> ViewType.DISCOVER_NEARBY.ordinal
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeListItem>() {

        override fun areItemsTheSame(
            oldItem: HomeListItem,
            newItem: HomeListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeListItem,
            newItem: HomeListItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface AdapterListener {
    fun onLocationPermissionClick()
    fun onCityItemClick(city: City)
    fun onScenicSpotItemClick(scenicSpotInfo: ScenicSpotInfo)
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

class CityContentViewHolder(
    viewBinding: ListItemCityBinding
) : ViewHolder(viewBinding.root) {

    private val adapter = HomeCityHorizontalListAdapter()
    private var listener: AdapterListener? = null

    init {
        viewBinding.viewRecycler.adapter = adapter
        viewBinding.viewRecycler.layoutManager = LinearLayoutManager(
            itemView.context, RecyclerView.HORIZONTAL, false
        )

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        adapter.setListener(object : HomeCityHorizontalListAdapter.AdapterListener {
            override fun onCityItemClick(city: City) {
                listener?.onCityItemClick(city)
            }
        })
    }

    companion object {
        fun newInstance(parent: ViewGroup): CityContentViewHolder {
            return CityContentViewHolder(
                ListItemCityBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: CityItems, listener: AdapterListener?) {
        this.listener = listener
        adapter.submitList(item.cities)
    }
}

class NearbyViewHolder(
    private val viewBinding: ListItemNearbyBinding
) : ViewHolder(viewBinding.root) {

    private var listener: AdapterListener? = null

    companion object {
        fun newInstance(parent: ViewGroup): NearbyViewHolder {
            return NearbyViewHolder(
                ListItemNearbyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    fun bindData(item: NearbyItems, listener: AdapterListener?) {
        this.listener = listener

        viewBinding.carouselView.apply {
            size = item.scenicSpots.size
            hideIndicator(false)
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

class DiscoverNearbyViewHolder(
    private val viewBinding: ListItemDiscoverNearbyBinding
) : ViewHolder(viewBinding.root) {

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