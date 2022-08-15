package com.scw.twtour.model.repository

import com.scw.twtour.model.data.*
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.util.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

interface ScenicSpotRepository {
    fun fetchItems(): Flow<List<ScenicSpotListItem>>
}

class ScenicSpotRepositoryImpl(
    private val localDataSource: ScenicSpotLocalDataSource
) : ScenicSpotRepository {

    companion object {
        private const val SCENIC_SPOT_LIMIT = 12
    }

    override fun fetchItems(): Flow<List<ScenicSpotListItem>> {
        return flowOf(mutableListOf<ScenicSpotListItem>())
            .combine(flowOf(TitleItem("北台灣"))) { list, title ->
                list.apply { add(title) }
            }
            .combine(queryRandomScenicSpotByCity(City.KEELUNG)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.KEELUNG.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.TAIPEI)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.TAIPEI.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.NEW_TAIPEI)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.NEW_TAIPEI.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.YILAN_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.YILAN_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.TAOYUAN)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.TAOYUAN.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.HSINCHU_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.HSINCHU_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.HSINCHU)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.HSINCHU.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(flowOf(TitleItem("中台灣"))) { list, title ->
                list.apply { add(title) }
            }
            .combine(queryRandomScenicSpotByCity(City.MIAOLI_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.MIAOLI_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.TAICHUNG)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.TAICHUNG.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.CHANGHUA_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.CHANGHUA_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.NANTOU_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.NANTOU_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.YUNLIN_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.YUNLIN_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(flowOf(TitleItem("南台灣"))) { list, title ->
                list.apply { add(title) }
            }
            .combine(queryRandomScenicSpotByCity(City.CHIAYI_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.CHIAYI_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.CHIAYI)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.CHIAYI.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.TAINAN)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.TAINAN.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.KAOHSIUNG)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.KAOHSIUNG.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.PINGTUNG_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.PINGTUNG_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(flowOf(TitleItem("東台灣"))) { list, title ->
                list.apply { add(title) }
            }
            .combine(queryRandomScenicSpotByCity(City.HUALIEN_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.HUALIEN_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.TAITUNG_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.TAITUNG_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(flowOf(TitleItem("離島地區"))) { list, title ->
                list.apply { add(title) }
            }
            .combine(queryRandomScenicSpotByCity(City.PENGHU_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.PENGHU_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.KINMEN_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.KINMEN_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .combine(queryRandomScenicSpotByCity(City.LIENCHIANG_COUNTRY)) { list, items ->
                list.apply {
                    add(SubtitleItem(City.LIENCHIANG_COUNTRY.value))
                    add(mappingToScenicSpotInfo(items))
                }
            }
            .flowOn(Dispatchers.IO)
    }

    private fun queryRandomScenicSpotByCity(city: City): Flow<List<ScenicSpotEntityItem>> {
        return localDataSource.queryRandomScenicSpotByCity(city.name, SCENIC_SPOT_LIMIT)
    }

    private fun mappingToScenicSpotInfo(items: List<ScenicSpotEntityItem>): MultipleItems {
        return MultipleItems(
            mutableListOf<ScenicSpotInfo>().apply {
                items.forEach { entity ->
                    add(ScenicSpotInfo().update(entity))
                }
            }
        )
    }

}