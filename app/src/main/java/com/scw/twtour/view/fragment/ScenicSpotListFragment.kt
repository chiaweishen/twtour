package com.scw.twtour.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.databinding.FragmentScenicSpotListBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
import com.scw.twtour.view.viewmodel.ScenicSpotListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ScenicSpotListFragment : Fragment() {

    private val args by navArgs<ScenicSpotListFragmentArgs>()
    private val viewModel by viewModel<ScenicSpotListViewModel>()

    private var _viewBinding: FragmentScenicSpotListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val pagingAdapter = ScenicSpotPagingAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private var queryTextChangeRunnable: Runnable? = null
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
        initRecyclerView()
        initSearchView()
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
                    ScenicSpotListFragmentDirections
                        .actionScenicSpotListFragmentToScenicSpotDetailsFragment(info.id)
                )
            }
        })
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
        queryTextChangeRunnable?.also { r ->
            handler.removeCallbacks(r)
        }
        queryTextChangeRunnable = handler.postDelayed(500) {
            if (lastQuery != query) {
                lastQuery = query
                collectData(query)
            }
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
                viewModel.getScenicSpotInfoList(
                    args.city,
                    args.zipCode,
                    query
                ).collectLatest { pagingData ->
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

