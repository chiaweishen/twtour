package com.scw.twtour.model.repository

import android.Manifest
import android.content.Context
import com.scw.twtour.model.data.*
import com.scw.twtour.model.datasource.local.LocationLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.util.ODataSelect
import com.scw.twtour.util.City
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import pub.devrel.easypermissions.EasyPermissions

interface ScenicSpotRepository {
    fun fetchItems(): Flow<List<HomeListItem>>
}

@FlowPreview
class ScenicSpotRepositoryImpl(
    private val context: Context,
    private val localDataSource: ScenicSpotLocalDataSource,
    private val locationLocalDataSource: LocationLocalDataSource,
    private val remoteDataSource: ScenicSpotRemoteDataSource
) : ScenicSpotRepository {

    companion object {
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
                list.apply {
                    add(TitleItem("北台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.TAIPEI, 0))
                            add(CityInfo(City.NEW_TAIPEI, 0))
                            add(CityInfo(City.KEELUNG, 0))
                            add(CityInfo(City.TAOYUAN, 0))
                            add(CityInfo(City.YILAN_COUNTRY, 0))
                            add(CityInfo(City.HSINCHU_COUNTRY, 0))
                            add(CityInfo(City.HSINCHU, 0))
                        }
                    ))
                    add(TitleItem("中台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.MIAOLI_COUNTRY, 0))
                            add(CityInfo(City.TAICHUNG, 0))
                            add(CityInfo(City.CHANGHUA_COUNTRY, 0))
                            add(CityInfo(City.NANTOU_COUNTRY, 0))
                            add(CityInfo(City.YUNLIN_COUNTRY, 0))
                        }
                    ))
                    add(TitleItem("南台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.CHIAYI_COUNTRY, 0))
                            add(CityInfo(City.CHIAYI, 0))
                            add(CityInfo(City.TAINAN, 0))
                            add(CityInfo(City.KAOHSIUNG, 0))
                            add(CityInfo(City.PINGTUNG_COUNTRY, 0))
                        }
                    ))
                    add(TitleItem("東台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.HUALIEN_COUNTRY, 0))
                            add(CityInfo(City.TAITUNG_COUNTRY, 0))
                        }
                    ))
                    add(TitleItem("離島地區"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.PENGHU_COUNTRY, 0))
                            add(CityInfo(City.KINMEN_COUNTRY, 0))
                            add(CityInfo(City.LIENCHIANG_COUNTRY, 0))
                            add(CityInfo(City.LANYU, 0))
                            add(CityInfo(City.LYUDAO, 0))
                            add(CityInfo(City.XIAOLIOUCHOU, 0))
                        }
                    ))
                }
            }
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
            .flatMapConcat { list ->
                remoteDataSource.getScenicSpotByIds(
                    list.map { it.id },
                    ODataSelect.Builder()
                        .add(ScenicSpotEntityItem::scenicSpotID.name)
                        .add(ScenicSpotEntityItem::scenicSpotName.name)
                        .add(ScenicSpotEntityItem::picture.name)
                        .build()
                ).map { items ->
                    mutableListOf<ScenicSpotInfo>().apply {
                        items.forEach { item ->
                            val distance: Int? = list.filter { it.id == item.scenicSpotID }.let {
                                it.firstOrNull()?.distanceMeter
                            }
                            distance?.also {
                                add(ScenicSpotInfo(distanceMeter = it).update(item))
                            }
                        }
                        sortBy { it.distanceMeter }
                    }
                }.map { nearbyInfo ->
                    mutableListOf<HomeListItem>().apply {
                        add(NearbyItems(nearbyInfo))
                    }
                }
            }
    }

}