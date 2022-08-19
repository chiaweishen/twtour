package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
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
        collectData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun initRecyclerView() {
        viewBinding.viewRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.viewRecycler.adapter = pagingAdapter

        pagingAdapter.setAdapterListener(object : ScenicSpotPagingAdapter.AdapterListener {
            override fun onItemClick(info: ScenicSpotInfo) {
                findNavController().navigate(
                    ScenicSpotListFragmentDirections
                        .actionScenicSpotListFragmentToScenicSpotDetailsFragment(info.id)
                )
            }
        })
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getScenicSpotInfoList(args.city, args.zipCode).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                Timber.i("Paging load states: $loadStates")
                updateLoadingState(loadStates)
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

