package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.databinding.FragmentScenicSpotNoteListBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.constant.NoteType
import com.scw.twtour.util.ScreenUtil
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
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
        initRecyclerView()
        initFloatingActionButton()
        collectDataWorkaround()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun initRecyclerView() {
        viewBinding.viewRecycler.adapter = pagingAdapter

        pagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        viewBinding.viewRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        pagingAdapter.setAdapterListener(object : ScenicSpotPagingAdapter.AdapterListener {
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
            }

            override fun onPushPinClick(info: ScenicSpotInfo) {
                viewModel.clickPushPin(info)
            }
        })

        viewBinding.viewRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offsetVertical = recyclerView.computeVerticalScrollOffset()
                val screenHeight = ScreenUtil.getScreenHeight(requireContext())
                updateFloatingActionButton(dy < 0 && offsetVertical > screenHeight)
            }
        })

        viewBinding.layoutSwipeRefresh.setOnRefreshListener {
            collectData()
        }
    }

    private fun initFloatingActionButton() {
        viewBinding.fab.setOnClickListener {
            val offsetVertical = viewBinding.viewRecycler.computeVerticalScrollOffset()
            val screenHeight = ScreenUtil.getScreenHeight(requireContext())
            if (offsetVertical > screenHeight * 5) {
                viewBinding.viewRecycler.scrollToPosition(0)
            } else {
                viewBinding.viewRecycler.smoothScrollToPosition(0)
            }
        }
    }

    private fun updateFloatingActionButton(show: Boolean) {
        if (show) {
            viewBinding.fab.show()
        } else {
            viewBinding.fab.hide()
        }
    }

    /** FIXME: Workaround
     * 跳轉頁面回來後，再次 collect 會取得先前 page 資料，導致 list view 位置被拉回
     * **/
    private var isDataCollected: Boolean = false
    private fun collectDataWorkaround() {
        if (!isDataCollected) {
            isDataCollected = true
            collectData()
        }
    }

    private fun collectData(query: String = "") {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.getNoteScenicSpotInfoList(noteType).collectLatest { pagingData ->
                    viewBinding.layoutSwipeRefresh.isRefreshing = false
                    pagingAdapter.submitData(pagingData)
                }
            }

            launch {
                viewLifecycleOwner.lifecycleScope.launch {
                    pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                        Timber.i("Paging load states: $loadStates")
                        updateLoadingState(loadStates)
                    }
                }
            }
        }
    }

    private fun updateLoadingState(loadStates: CombinedLoadStates) {
        viewBinding.linearProgressIndicator.visibility =
            if (loadStates.append is LoadState.Loading) View.VISIBLE else View.GONE

        val errorState = when {
            loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
            loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
            loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
            else -> null
        }
        errorState?.also {
            // TODO
        }
    }
}

