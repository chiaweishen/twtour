package com.scw.twtour.view.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.MyApplication
import com.scw.twtour.databinding.FragmentHomeBinding
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.ext.launchAndCollect
import com.scw.twtour.util.AccessFineLocationGranted
import com.scw.twtour.util.City
import com.scw.twtour.util.SyncComplete
import com.scw.twtour.view.adapter.AdapterListener
import com.scw.twtour.view.adapter.HomeListAdapter
import com.scw.twtour.view.viewmodel.HomeViewModel
import com.scw.twtour.view.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pub.devrel.easypermissions.EasyPermissions

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

        homeListAdapter.setListener(object : AdapterListener {
            override fun onLocationPermissionClick() {
                checkAccessLocationFinePermission()
            }

            override fun onCityItemClick(city: City?, zipCode: Int?) {
                city?.also {
                    val directions = HomeFragmentDirections
                        .actionHomeFragmentToScenicSpotListFragment(it, zipCode ?: -1)
                    findNavController().navigate(directions)
                }
            }
        })

        viewBinding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.fetchScenicSpotItems()
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.syncState.launchAndCollect { state ->
                    if (state == SyncComplete) {
                        if (viewModel.listItems.value.isEmpty()) {
                            viewModel.fetchScenicSpotItems()
                        } else {
                            homeListAdapter.submitList(viewModel.listItems.value)
                        }
                    }
                }

                viewModel.listItems.launchAndCollect { items ->
                    viewBinding.layoutSwipeRefresh.isRefreshing = false
                    homeListAdapter.submitList(items)
                }

                // FIXME: 收不到 stateFlow 變更事件！？
                mainViewModel.permissionState.launchAndCollect { state ->
                    if (state == AccessFineLocationGranted) {
                        viewModel.fetchScenicSpotItems()
                    }
                }
            }
        }
    }

    private fun checkAccessLocationFinePermission() {
        if (!EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            EasyPermissions.requestPermissions(
                this@HomeFragment,
                "請允許「位置權限」\n用以支援完整景點功能",
                222,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            viewModel.fetchScenicSpotItems()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}