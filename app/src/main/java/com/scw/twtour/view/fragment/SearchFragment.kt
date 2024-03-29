package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.scw.twtour.R
import com.scw.twtour.constant.City
import com.scw.twtour.databinding.FragmentSearchBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.VibrateUtil
import com.scw.twtour.util.ZipCodeUtil
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
import com.scw.twtour.view.util.ScenicSpotPagingLayoutManager
import com.scw.twtour.view.viewmodel.ScenicSpotListViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@FlowPreview
class SearchFragment : Fragment() {

    private val viewModel by viewModel<ScenicSpotListViewModel>()

    private var _viewBinding: FragmentSearchBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var layoutManager: ScenicSpotPagingLayoutManager
    private val pagingAdapter = ScenicSpotPagingAdapter()

    private var lastQuery: String = ""
    private var city: City = City.ALL
    private var lastCity: City = city

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectNoteState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.city, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        city = City.fromValue(item.title.toString()) ?: City.ALL
        updateCityView()
        query(lastQuery, city)
        return super.onContextItemSelected(item)
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
                    SearchFragmentDirections
                        .actionSearchFragmentToScenicSpotDetailsFragment(info.id, info.name)
                )
            }

            override fun onStarClick(info: ScenicSpotInfo) {
                viewModel.clickStar(info)
                VibrateUtil.tick(requireContext())
            }

            override fun onPushPinClick(info: ScenicSpotInfo) {
                viewModel.clickPushPin(info)
                VibrateUtil.tick(requireContext())
            }
        })

        updateCityView()
        updateEmptyView()
        initContextMenu()
        initSearchView()
    }

    private fun updateCityView() {
        viewBinding.btnCity.text = city.value
    }

    private fun updateEmptyView() {
        if (lastQuery.isNotBlank()) {
            viewBinding.textEmpty.visibility = View.GONE
        } else {
            viewBinding.textEmpty.visibility = View.VISIBLE
        }
    }

    private fun initContextMenu() {
        registerForContextMenu(viewBinding.btnCity)
        viewBinding.btnCity.setOnClickListener { view ->
            view.showContextMenu()
        }
    }

    private fun initSearchView() {
        viewBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query(newText, lastCity)
                return true
            }
        })
    }

    private fun query(query: String, city: City) {
        Timber.i("query: $query")
        if (query.isNotBlank()) {
            if (lastQuery != query || lastCity != city) {
                lastQuery = query
                lastCity = city
                collectPagingData(query, city)
            }
        } else {
            viewBinding.textEmpty.visibility = View.VISIBLE
            clearData()
        }
    }

    private fun clearData() {
        lastQuery = ""
        pagingAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
    }

    private fun collectPagingData(query: String, city: City) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getScenicSpotInfoList(
                city,
                ZipCodeUtil.getOutlingIslandsZipCode(city),
                query
            ).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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

