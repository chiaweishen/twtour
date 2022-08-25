package com.scw.twtour.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.scw.twtour.util.NoteType
import com.scw.twtour.view.fragment.ScenicSpotNoteListFragment
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ScenicSpotNoteTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return ScenicSpotNoteListFragment.newInstance(
            if (position == 0) {
                NoteType.PUSH_PIN
            } else {
                NoteType.STAR
            }
        )
    }
}