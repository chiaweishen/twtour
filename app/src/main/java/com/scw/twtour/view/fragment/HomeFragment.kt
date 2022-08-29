@file:OptIn(FlowPreview::class)

package com.scw.twtour.view.fragment

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.R
import com.scw.twtour.constant.City
import com.scw.twtour.databinding.FragmentHomeBinding
import com.scw.twtour.ext.launchAndCollect
import com.scw.twtour.model.data.Result
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.data.SyncComplete
import com.scw.twtour.util.ZipCodeUtil
import com.scw.twtour.view.adapter.AdapterListener
import com.scw.twtour.view.adapter.HomeListAdapter
import com.scw.twtour.view.viewmodel.HomeViewModel
import com.scw.twtour.view.viewmodel.MainViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class HomeFragment : Fragment() {

    private val mainViewModel by viewModel<MainViewModel>()

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
        initMenu()
        initView()
        collectData()
    }

    override fun onStart() {
        super.onStart()
        if (hasAccessFineLocationPermission()) {
            viewModel.fetchScenicSpotItems()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (hasAccessFineLocationPermission()) {
            viewModel.fetchScenicSpotItems()

            Handler(Looper.getMainLooper()).postDelayed({
                viewBinding.viewRecycler.smoothScrollToPosition(0)
            }, 500)
        }
    }

    private fun initView() {
        viewBinding.viewRecycler.adapter = homeListAdapter

        viewBinding.viewRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        homeListAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        homeListAdapter.setListener(object : AdapterListener {
            override fun onLocationPermissionClick() {
                checkAccessLocationFinePermission()
            }

            override fun onCityItemClick(city: City) {
                val zipCode = ZipCodeUtil.getOutlingIslandsZipCode(city)
                findNavController().navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToScenicSpotListFragment(city, zipCode)
                )
            }

            override fun onScenicSpotItemClick(scenicSpotInfo: ScenicSpotInfo) {
                findNavController().navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToScenicSpotDetailsFragment(
                            scenicSpotInfo.id, scenicSpotInfo.name
                        )
                )
            }
        })

        viewBinding.layoutSwipeRefresh.setOnRefreshListener {
            viewModel.fetchScenicSpotItems()
        }
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return menuItem.onNavDestinationSelected(findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.syncState.launchAndCollect { state ->
                    if (state == SyncComplete) {
                        viewModel.fetchScenicSpotItems()
                    }
                }
            }
        }

        viewModel.listItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    viewBinding.linearProgressIndicator.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    viewBinding.linearProgressIndicator.visibility = View.GONE
                    viewBinding.layoutSwipeRefresh.isRefreshing = false
                    homeListAdapter.submitList(result.value)
                }
                is Result.Error -> {
                    viewBinding.linearProgressIndicator.visibility = View.GONE
                    viewBinding.layoutSwipeRefresh.isRefreshing = false
                    Timber.e(Log.getStackTraceString(result.e))
                }
            }

        }
    }

    private fun checkAccessLocationFinePermission() {
        if (!hasAccessFineLocationPermission()) {
            EasyPermissions.requestPermissions(
                this@HomeFragment,
                "請允許「位置權限」\n用以支援完整景點功能",
                222,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun hasAccessFineLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

}