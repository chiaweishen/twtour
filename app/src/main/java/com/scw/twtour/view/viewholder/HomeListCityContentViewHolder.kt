package com.scw.twtour.view.viewholder

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.constant.City
import com.scw.twtour.databinding.ListItemCityBinding
import com.scw.twtour.model.data.CityItems
import com.scw.twtour.view.adapter.AdapterListener
import com.scw.twtour.view.adapter.HomeCityHorizontalListAdapter

class CityContentViewHolder(
    private val viewBinding: ListItemCityBinding,
    private val lastPositionMap: SparseIntArray,
    private val lastOffsetMap: SparseIntArray,
    listener: AdapterListener?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private val homeCityAdapter = HomeCityHorizontalListAdapter()
    private var itemPosition: Int = 0

    init {
        viewBinding.viewRecycler.apply {
            this.adapter = homeCityAdapter
            layoutManager = LinearLayoutManager(
                itemView.context, RecyclerView.HORIZONTAL, false
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    (layoutManager as? LinearLayoutManager)?.also { manager ->
                        val firstItemPosition = manager.findFirstVisibleItemPosition()
                        val firstChildView = manager.findViewByPosition(firstItemPosition)
                        firstChildView?.apply {
                            lastPositionMap.put(itemPosition, firstItemPosition)
                            lastOffsetMap.put(
                                itemPosition,
                                firstChildView.x.toInt() - recyclerView.paddingRight
                            )
                        }
                    }
                }
            })
        }

        homeCityAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        homeCityAdapter.setListener(object : HomeCityHorizontalListAdapter.AdapterListener {
            override fun onCityItemClick(city: City) {
                listener?.onCityItemClick(city)
            }
        })
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            lastPositionMap: SparseIntArray,
            lastOffsetMap: SparseIntArray,
            listener: AdapterListener?
        ): CityContentViewHolder {
            return CityContentViewHolder(
                ListItemCityBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                lastPositionMap, lastOffsetMap, listener
            )
        }
    }

    fun bindData(item: CityItems) {
        itemPosition = bindingAdapterPosition
        homeCityAdapter.submitList(item.cities)

        (viewBinding.viewRecycler.layoutManager as? LinearLayoutManager)?.also { manager ->
            manager.scrollToPositionWithOffset(
                lastPositionMap.get(itemPosition),
                lastOffsetMap.get(itemPosition)
            )
        }
    }
}