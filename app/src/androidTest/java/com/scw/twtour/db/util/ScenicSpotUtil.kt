package com.scw.twtour.db.util

import com.scw.twtour.http.data.City
import com.scw.twtour.modole.entity.Position
import com.scw.twtour.modole.entity.ScenicSpotEntityItem

object ScenicSpotUtil {

    fun getScenicSpot(): ScenicSpotEntityItem {
        return ScenicSpotEntityItem(
            scenicSpotID = "C1_382000000A_209657",
            scenicSpotName = "淡水金色水岸",
            zipCode = 251,
            position = Position(
                positionLon = 121.4437484741211,
                positionLat = 25.16786003112793
            ),
            city = City.NEW_TAIPEI
        )
    }

    fun getScenicSpots(): List<ScenicSpotEntityItem> {
        return mutableListOf<ScenicSpotEntityItem>().apply {
            add(
                ScenicSpotEntityItem(
                    scenicSpotID = "C1_315081300H_000184",
                    scenicSpotName = "三代木及象鼻木",
                    city = City.CHIAYI_COUNTRY
                )
            )
            add(
                ScenicSpotEntityItem(
                    scenicSpotID = "C1_376530000A_000211",
                    scenicSpotName = "門馬羅山",
                    zipCode = 947,
                    position = Position(
                        positionLon = 120.8116683959961,
                        positionLat = 21.99333381652832
                    ),
                    city = City.PINGTUNG_COUNTRY
                )
            )
        }
    }
}