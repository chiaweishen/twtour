package com.scw.twtour.network.util

import com.scw.twtour.util.City

object ODataFilter {
    object ScenicSpot {
        fun byCity(city: City, query: String = ""): String {
            return city.value.let {
                "(City eq '$it' or startswith(Address, '$it')) AND contains(ScenicSpotName, '$query')"
            }
        }

        fun byZipCode(zipCode: Int, query: String = ""): String {
            return "(ZipCode eq '$zipCode') AND contains(ScenicSpotName, '$query')"
        }

        fun byId(id: String): String {
            return "ScenicSpotID eq '$id'"
        }

        fun byIds(ids: List<String>): String {
            val list = mutableListOf<String>()
            ids.forEach { id ->
                list.add(byId(id))
            }
            return list.joinToString(" OR ")
        }
    }
}