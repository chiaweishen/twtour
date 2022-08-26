package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scw.twtour.R
import com.scw.twtour.constant.City
import com.scw.twtour.databinding.FragmentSearchBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.ScreenUtil
import com.scw.twtour.util.ZipCodeUtil
import com.scw.twtour.view.adapter.ScenicSpotPagingAdapter
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
        initRecyclerView()
        initSearchView()
        initFloatingActionButton()
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
        city = when (item.itemId) {
            R.id.city_taipei -> City.TAIPEI
            R.id.city_new_taipei -> City.NEW_TAIPEI
            R.id.city_taoyuan -> City.TAOYUAN
            R.id.city_taichung -> City.TAICHUNG
            R.id.city_tainan -> City.TAINAN
            R.id.city_kaoshiung -> City.KAOHSIUNG
            R.id.city_keelung -> City.KEELUNG
            R.id.city_hsinchu -> City.HSINCHU
            R.id.city_hsinchu_country -> City.HSINCHU_COUNTRY
            R.id.city_miaoli_country -> City.MIAOLI_COUNTRY
            R.id.city_changhua_country -> City.CHANGHUA_COUNTRY
            R.id.city_nantou_country -> City.NANTOU_COUNTRY
            R.id.city_yunlin_country -> City.YUNLIN_COUNTRY
            R.id.city_chiayi -> City.CHIAYI
            R.id.city_chiayi_country -> City.CHIAYI_COUNTRY
            R.id.city_pingtung_country -> City.PINGTUNG_COUNTRY
            R.id.city_yilan_country -> City.YILAN_COUNTRY
            R.id.city_hualien_country -> City.HUALIEN_COUNTRY
            R.id.city_taitung_country -> City.TAITUNG_COUNTRY
            R.id.city_kimen_country -> City.KINMEN_COUNTRY
            R.id.city_penghu_country -> City.PENGHU_COUNTRY
            R.id.city_lienchiang_country -> City.LIENCHIANG_COUNTRY
            R.id.city_lanyu -> City.LANYU
            R.id.city_lyudao -> City.LYUDAO
            R.id.city_xiaoliouchou -> City.XIAOLIOUCHOU
            else -> City.ALL
        }
        updateCityView()
        query(lastQuery, city)
        return super.onContextItemSelected(item)
    }

    private fun initView() {
        registerForContextMenu(viewBinding.btnCity)
        viewBinding.btnCity.setOnClickListener { view ->
            view.showContextMenu()
        }
        updateCityView()
    }

    private fun updateCityView() {
        viewBinding.btnCity.text = city.value
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
                    SearchFragmentDirections
                        .actionSearchFragmentToScenicSpotDetailsFragment(info.id, info.name)
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
                collectData(query, city)
            }
        } else {
            viewBinding.textEmpty.text = "請輸入景點名稱關鍵字進行搜尋"
            viewBinding.textEmpty.visibility = View.VISIBLE
            clearData()
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

    private fun clearData() {
        lastQuery = ""
        pagingAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
    }

    private fun collectData(query: String, city: City) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.getScenicSpotInfoList(
                    city,
                    ZipCodeUtil.getOutlingIslandsZipCode(city),
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
        if (loadStates.source.refresh is LoadState.NotLoading &&
            loadStates.append.endOfPaginationReached &&
            pagingAdapter.itemCount < 1
        ) {
            if (viewBinding.searchView.query.isNotBlank()) {
                viewBinding.textEmpty.text = "目前景點關鍵字\n沒有搜尋到資料"
            } else {
                viewBinding.textEmpty.text = "請輸入景點名稱關鍵字進行搜尋"
            }
            viewBinding.textEmpty.visibility = View.VISIBLE
        } else {
            viewBinding.textEmpty.visibility = View.GONE
        }

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

