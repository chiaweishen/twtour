package com.scw.twtour.model.repository

import android.Manifest
import android.content.Context
import com.scw.twtour.R
import com.scw.twtour.constant.City
import com.scw.twtour.model.data.*
import com.scw.twtour.model.datasource.local.LocationLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.util.ODataSelect
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
                            add(CityInfo(City.TAIPEI, R.drawable.taipei))
                            add(CityInfo(City.NEW_TAIPEI, R.drawable.new_taipei))
                            add(CityInfo(City.KEELUNG, R.drawable.keelung))
                            add(CityInfo(City.TAOYUAN, R.drawable.taoyuan))
                            add(CityInfo(City.YILAN_COUNTRY, R.drawable.yilan))
                            add(CityInfo(City.HSINCHU_COUNTRY, R.drawable.hsinchu_country))
                            add(CityInfo(City.HSINCHU, R.drawable.hsinchu))
                        }
                    ))
                    add(TitleItem("中台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.MIAOLI_COUNTRY, R.drawable.miaoli_country))
                            add(CityInfo(City.TAICHUNG, R.drawable.taichung))
                            add(CityInfo(City.CHANGHUA_COUNTRY, R.drawable.changhua))
                            add(CityInfo(City.NANTOU_COUNTRY, R.drawable.nantou_country))
                            add(CityInfo(City.YUNLIN_COUNTRY, R.drawable.yunlin_country))
                        }
                    ))
                    add(TitleItem("南台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.CHIAYI_COUNTRY, R.drawable.chiayi_country))
                            add(CityInfo(City.CHIAYI, R.drawable.chiayi))
                            add(CityInfo(City.TAINAN, R.drawable.tainan))
                            add(CityInfo(City.KAOHSIUNG, R.drawable.kaohshiung))
                            add(CityInfo(City.PINGTUNG_COUNTRY, R.drawable.pingtung_country))
                        }
                    ))
                    add(TitleItem("東台灣"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.HUALIEN_COUNTRY, R.drawable.hualien_country))
                            add(CityInfo(City.TAITUNG_COUNTRY, R.drawable.taitung_country))
                        }
                    ))
                    add(TitleItem("離島地區"))
                    add(CityItems(
                        mutableListOf<CityInfo>().apply {
                            add(CityInfo(City.PENGHU_COUNTRY, R.drawable.penghu_country))
                            add(CityInfo(City.KINMEN_COUNTRY, R.drawable.kinmen_country))
                            add(
                                CityInfo(
                                    City.LIENCHIANG_COUNTRY,
                                    R.drawable.lienchiang_country
                                )
                            )
                            add(CityInfo(City.LANYU, R.drawable.lanyu))
                            add(CityInfo(City.LYUDAO, R.drawable.lyudao))
                            add(CityInfo(City.XIAOLIOUCHOU, R.drawable.xiaoliochou))
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
                }
            }
            .flatMapConcat { nearbyInfo ->
                localDataSource.clearInvalidNote()
                localDataSource.queryNotes(nearbyInfo.map { it.id }.toTypedArray())
                    .map { noteEntities ->
                        noteEntities.forEach { noteEntity ->
                            nearbyInfo.filter { it.id == noteEntity.id }.also {
                                it.firstOrNull()?.update(noteEntity)
                            }
                        }
                        mutableListOf<HomeListItem>().apply {
                            add(NearbyItems(nearbyInfo))
                        }
                    }
            }
    }

}