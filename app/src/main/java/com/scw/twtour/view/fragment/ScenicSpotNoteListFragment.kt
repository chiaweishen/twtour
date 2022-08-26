package com.scw.twtour.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.scw.twtour.constant.NoteType
import com.scw.twtour.databinding.FragmentScenicSpotNoteListBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
import com.scw.twtour.view.util.ScenicSpotPagingLayoutManager
import com.scw.twtour.view.viewmodel.ScenicSpotNoteListViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@FlowPreview
class ScenicSpotNoteListFragment : Fragment() {

    private val viewModel by viewModel<ScenicSpotNoteListViewModel>()

    private var _viewBinding: FragmentScenicSpotNoteListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var layoutManager: ScenicSpotPagingLayoutManager
    private lateinit var pagingAdapter: ScenicSpotPagingAdapter

    private lateinit var noteType: NoteType

    companion object {
        fun newInstance(type: NoteType): ScenicSpotNoteListFragment {
            return ScenicSpotNoteListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("type", type)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteType = arguments?.getSerializable("type") as NoteType
        pagingAdapter = ScenicSpotPagingAdapter(
            if (noteType == NoteType.PUSH_PIN) {
                ScenicSpotPagingAdapter.NoteViewSource.PUSH_PIN
            } else {
                ScenicSpotPagingAdapter.NoteViewSource.STAR
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentScenicSpotNoteListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun initView() {
        layoutManager = ScenicSpotPagingLayoutManager(
            viewBinding.viewRecycler,
            viewBinding.fab,
            pagingAdapter,
            viewBinding.textEmpty,
            viewBinding.linearProgressIndicator
        )
        layoutManager.initView(object : ScenicSpotPagingLayoutManager.AdapterListener {
            override fun onItemClick(info: ScenicSpotInfo) {
                findNavController().navigate(
                    ScenicSpotNoteTabFragmentDirections
                        .actionScenicSpotNoteTabFragmentToScenicSpotDetailsFragment(
                            info.id,
                            info.name
                        )
                )
            }

            override fun onStarClick(info: ScenicSpotInfo) {
                viewModel.clickStar(info)
                collectData()
            }

            override fun onPushPinClick(info: ScenicSpotInfo) {
                viewModel.clickPushPin(info)
                collectData()
            }
        })

        updateEmptyView()
    }

    private fun updateEmptyView() {
        viewBinding.textEmpty.text =
            if (noteType == NoteType.PUSH_PIN) {
                "目前沒有釘選資料"
            } else {
                "目前沒有星號資料"
            }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.getNoteScenicSpotInfoList(noteType).collectLatest { pagingData ->
                    pagingAdapter.submitData(pagingData)
                }
            }

            launch {
                viewLifecycleOwner.lifecycleScope.launch {
                    pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                        layoutManager.updateLoadingState(loadStates)
                    }
                }
            }
        }
    }

}

