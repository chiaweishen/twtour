package com.scw.twtour.model.repository

import android.Manifest
import android.content.Context
import com.scw.twtour.model.data.*
import com.scw.twtour.model.datasource.local.LocationLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.util.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import pub.devrel.easypermissions.EasyPermissions

interface ScenicSpotRepository {
    fun fetchItems(): Flow<List<HomeListItem>>
}

@FlowPreview
class ScenicSpotRepositoryImpl(
    private val context: Context,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val locationLocalDataSource: LocationLocalDataSource
) : ScenicSpotRepository {

    companion object {
        private const val CITY_SCENIC_SPOT_LIMIT = 1
        private const val NEARBY_SCENIC_SPOT_LIMIT = 20
    }

    override fun fetchItems(): Flow<List<HomeListItem>> {
        return if (hasAccessLocationPermission()) {
            nearbyFlow()
        } else {
            flowOf(
                mutableListOf<HomeListItem>().apply {
                    add(DiscoverNearByItem)
                }
            )
        }
            .map { list ->
                list.apply { add(TitleItem("北台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.TAIPEI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.NEW_TAIPEI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.KEELUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.TAOYUAN)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.YILAN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.HSINCHU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
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
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.MIAOLI_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.TAICHUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.CHANGHUA_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.NANTOU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
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
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.TAINAN)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.KAOHSIUNG)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
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
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.HUALIEN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
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
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.PENGHU_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.KINMEN_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        queryRandomScenicSpotsHasImageByCity(City.LIENCHIANG_COUNTRY)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        localDataSource.queryRandomScenicSpotsHasImageInLanyu(CITY_SCENIC_SPOT_LIMIT)
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
                        localDataSource.queryRandomScenicSpotsHasImageInLyudao(
                            CITY_SCENIC_SPOT_LIMIT
                        )
                    ) { scenicSpotInfoList, randomInfoList ->
                        scenicSpotInfoList.apply { add(randomInfoList.first()) }
                    }
                    .zip(
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

    private fun hasAccessLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) || EasyPermissions.hasPermissions(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun nearbyFlow(): Flow<MutableList<HomeListItem>> {
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
            .zip(flowOf(mutableListOf<HomeListItem>())) { nearbyInfo, list ->
                list.apply {
                    add(NearbyItems(nearbyInfo))
                }
            }
    }

}