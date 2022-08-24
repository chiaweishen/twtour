package com.scw.twtour.network.util

import com.scw.twtour.util.City

object ODataFilter {
    object ScenicSpot {

        fun queryByNameKeyword(query: String): String {
            return "contains(ScenicSpotName, '$query')"
        }

        fun queryByCityAndNameKeyword(city: City, query: String = ""): String {
            return city.value.let {
                "(City eq '$it' or startswith(Address, '$it')) AND contains(ScenicSpotName, '$query')"
            }
        }

        fun queryByZipCodeAndNameKeyword(zipCode: Int, query: String = ""): String {
            return "(ZipCode eq '$zipCode') AND contains(ScenicSpotName, '$query')"
        }

        fun queryById(id: String): String {
            return "ScenicSpotID eq '$id'"
        }

        fun queryByIdList(ids: List<String>): String {
            val list = mutableListOf<String>()
            ids.forEach { id ->
                list.add(queryById(id))
            }
            return list.joinToString(" OR ")
        }
    }
}