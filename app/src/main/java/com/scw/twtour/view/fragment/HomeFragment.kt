package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.MyApplication
import com.scw.twtour.databinding.FragmentHomeBinding
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.util.SyncComplete
import com.scw.twtour.view.adapter.HomeListAdapter
import com.scw.twtour.view.viewmodel.HomeViewModel
import com.scw.twtour.view.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : Fragment() {

    private val mainViewModel by viewModel<MainViewModel> {
        parametersOf(get<AuthUseCase> {
            parametersOf(
                MyApplication.getClientId(),
                MyApplication.getClientSecret()
            )
        })
    }

    private val viewModel by viewModel<HomeViewModel>()

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding: FragmentHomeBinding get() = _viewBinding!!

    private val homeListAdapter = HomeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectData()
    }

    private fun initView() {
        viewBinding.viewRecycler.adapter = homeListAdapter
        viewBinding.viewRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.syncState.collect { state ->
                if (state == SyncComplete) {
                    viewModel.fetchScenicSpotItems()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.scenicSpotItems.collect { items ->
                homeListAdapter.submitList(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}