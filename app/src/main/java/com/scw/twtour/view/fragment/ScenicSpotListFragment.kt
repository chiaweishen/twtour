package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.scw.twtour.MainActivity
import com.scw.twtour.databinding.FragmentScenicSpotListBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
import com.scw.twtour.view.util.ScenicSpotPagingLayoutManager
import com.scw.twtour.view.viewmodel.ScenicSpotListViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@FlowPreview
class ScenicSpotListFragment : Fragment() {

    private val args by navArgs<ScenicSpotListFragmentArgs>()
    private val viewModel by viewModel<ScenicSpotListViewModel>()

    private var _viewBinding: FragmentScenicSpotListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var layoutManager: ScenicSpotPagingLayoutManager
    private val pagingAdapter = ScenicSpotPagingAdapter()
    private var lastQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentScenicSpotListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectPagingDataWorkaround()
        collectNoteState()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setActionBarTitle(args.city.value)
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
                    ScenicSpotListFragmentDirections
                        .actionScenicSpotListFragmentToScenicSpotDetailsFragment(info.id, info.name)
                )
            }

            override fun onStarClick(info: ScenicSpotInfo) {
                viewModel.clickStar(info)
            }

            override fun onPushPinClick(info: ScenicSpotInfo) {
                viewModel.clickPushPin(info)
            }
        })

        initSearchView()
    }

    private fun initSearchView() {
        viewBinding.searchFilterView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Timber.i("onQueryTextChange: $newText")
                collectFilterData(newText)
                return true
            }
        })
    }

    private fun collectFilterData(query: String) {
        if (lastQuery != query) {
            lastQuery = query
            collectPagingData(query)
        }
    }

    /** FIXME: Workaround
     * 跳轉頁面回來後，再次 collect 會取得初始 page 資料，導致 list view 位置被拉回
     * 但此解法會造成下一頁共用資料變更，無法在返回後同步取得
     * **/
    private var isDataCollected: Boolean = false
    private fun collectPagingDataWorkaround() {
        if (!isDataCollected) {
            isDataCollected = true
            collectPagingData()
        }
    }

    private fun collectPagingData(query: String = "") {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getScenicSpotInfoList(
                args.city,
                args.zipCode,
                query
            ).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                layoutManager.updateLoadingState(loadStates)
            }
        }
    }

    private fun collectNoteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noteStateChanged.collectLatest {
                pagingAdapter.updateNoteState(it)
            }
        }
    }

}

