package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.scw.twtour.R
import com.scw.twtour.view.adapter.ScenicSpotNoteTabAdapter
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ScenicSpotNoteTabFragment : Fragment() {

    private lateinit var adapter: ScenicSpotNoteTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_scenic_spot_note_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        adapter = ScenicSpotNoteTabAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(view.findViewById(R.id.tab_layout), viewPager) { tab, position ->
            tab.setIcon(
                if (position == 0) {
                    R.drawable.ic_baseline_push_pin_24
                } else {
                    R.drawable.ic_baseline_stars_24
                }
            )
        }.attach()
    }
}