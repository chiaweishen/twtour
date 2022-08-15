package com.scw.twtour.model.repository

import com.scw.twtour.model.data.MultipleItems
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.model.data.ScenicSpotListItem
import com.scw.twtour.model.data.TitleItem
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.util.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

interface ScenicSpotRepository {
    fun fetchItems(): Flow<List<ScenicSpotListItem>>
}

@FlowPreview
class ScenicSpotRepositoryImpl(
    private val localDataSource: ScenicSpotLocalDataSource
) : ScenicSpotRepository {

    companion object {
        private const val SCENIC_SPOT_LIMIT = 1
    }

    override fun fetchItems(): Flow<List<ScenicSpotListItem>> {

        return flowOf(mutableListOf<ScenicSpotListItem>())
            .map { list ->
                list.apply { add(TitleItem("北台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAIPEI)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.TAIPEI, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.NEW_TAIPEI)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.NEW_TAIPEI, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KEELUNG)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.KEELUNG, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAOYUAN)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.TAOYUAN, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.YILAN_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.YILAN_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HSINCHU_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.HSINCHU_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HSINCHU)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.HSINCHU, entityItems))
                        }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(MultipleItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("中台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.MIAOLI_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.MIAOLI_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAICHUNG)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.TAICHUNG, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHANGHUA_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.CHANGHUA_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.NANTOU_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.NANTOU_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.YUNLIN_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.YUNLIN_COUNTRY, entityItems))
                        }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(MultipleItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("南台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.CHIAYI_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.CHIAYI)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.CHIAYI, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAINAN)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.TAINAN, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KAOHSIUNG)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.KAOHSIUNG, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.PINGTUNG_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.PINGTUNG_COUNTRY, entityItems))
                        }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(MultipleItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("東台灣")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.HUALIEN_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.HUALIEN_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.TAITUNG_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.TAITUNG_COUNTRY, entityItems))
                        }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(MultipleItems(scenicSpotInfoList)) }
                    }
            }
            .map { list ->
                list.apply { add(TitleItem("離島地區")) }
            }
            .flatMapConcat { list ->
                flowOf(mutableListOf<ScenicSpotInfo>())
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.PENGHU_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.PENGHU_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.KINMEN_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.KINMEN_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        queryRandomScenicSpotsHasImageByCity(City.LIENCHIANG_COUNTRY)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            add(mappingToScenicSpotInfo(City.LIENCHIANG_COUNTRY, entityItems))
                        }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInLanyu(SCENIC_SPOT_LIMIT)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            val info = mappingToScenicSpotInfo(City.LANYU, entityItems)
                            info.city = City.LANYU.value
                            add(info)
                        }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInLyudao(SCENIC_SPOT_LIMIT)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            val info = mappingToScenicSpotInfo(City.LYUDAO, entityItems)
                            info.city = City.LYUDAO.value
                            add(info)
                        }
                    }
                    .combine(
                        localDataSource.queryRandomScenicSpotsHasImageInXiaoliouchou(SCENIC_SPOT_LIMIT)
                    ) { scenicSpotInfoList, entityItems ->
                        scenicSpotInfoList.apply {
                            val info = mappingToScenicSpotInfo(City.XIAOLIOUCHOU, entityItems)
                            info.city = City.XIAOLIOUCHOU.value
                            add(info)
                        }
                    }
                    .map { scenicSpotInfoList ->
                        list.apply { add(MultipleItems(scenicSpotInfoList)) }
                    }
            }
            .flowOn(Dispatchers.IO)
    }

    private fun queryRandomScenicSpotsHasImageByCity(city: City): Flow<List<ScenicSpotEntityItem>> {
        return localDataSource.queryRandomScenicSpotsHasImageByCity(city.name, SCENIC_SPOT_LIMIT)
    }

    private fun mappingToScenicSpotInfo(
        city: City,
        items: List<ScenicSpotEntityItem>
    ): ScenicSpotInfo {
        return items.firstOrNull()?.let {
            ScenicSpotInfo().update(it)
        } ?: ScenicSpotInfo(city = city.value)
    }

}