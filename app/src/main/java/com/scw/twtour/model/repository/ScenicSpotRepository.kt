package com.scw.twtour.model.repository

import com.scw.twtour.model.data.*
import com.scw.twtour.model.datasource.local.LocationLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.util.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber

interface ScenicSpotRepository {
    fun fetchItems(): Flow<List<HomeListItem>>
}

@FlowPreview
class ScenicSpotRepositoryImpl(
    private val localDataSource: ScenicSpotLocalDataSource,
    private val locationLocalDataSource: LocationLocalDataSource
) : ScenicSpotRepository {

    companion object {
        private const val CITY_SCENIC_SPOT_LIMIT = 1
        private const val NEARBY_SCENIC_SPOT_LIMIT = 20
    }

    override fun fetchItems(): Flow<List<HomeListItem>> {
        return locationLocalDataSource.getLastLocation()
            .flatMapConcat { location ->
                location?.let {
                    localDataSource.queryNearbyScenicSpots(
                        it.latitude,
                        it.longitude,
                        NEARBY_SCENIC_SPOT_LIMIT
                    )
                } ?: run {
                    flowOf(emptyList())
                }
            }
            .combine(flowOf(mutableListOf<HomeListItem>())) { nearbyInfo, list ->
                list.apply {
                    add(NearbyItems(nearbyInfo))
                }
            }
            .map { list ->
                Timber.d("list: $list")
                list.apply { add(TitleItem("北台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAIPEI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.NEW_TAIPEI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KEELUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAOYUAN)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.YILAN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HSINCHU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HSINCHU)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(CityItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("中台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.MIAOLI_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAICHUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHANGHUA_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.NANTOU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.YUNLIN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(CityItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("南台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAINAN)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KAOHSIUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.PINGTUNG_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(CityItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("東台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HUALIEN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAITUNG_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(CityItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("離島地區")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.PENGHU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KINMEN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.LIENCHIANG_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInLanyu(CITY_SCENIC_SPOT_LIMIT)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInLyudao(
                            CITY_SCENIC_SPOT_LIMIT
                        )
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInXiaoliouchou(
                            CITY_SCENIC_SPOT_LIMIT
                        )
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(CityItems(scenicSpotInfoList)) }
                    }
            }
            .flowOn(Dispatchers.IO)
    }

    private fun queryRandomScenicSpotsHasImageByCity(city: City): Flow<List<ScenicSpotInfo>> {
        return localDataSource.queryRandomScenicSpotsHasImageByCity(city, CITY_SCENIC_SPOT_LIMIT)
    }

}