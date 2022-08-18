package com.scw.twtour.network.util

import com.scw.twtour.util.City

object ODataFilter {
    object ScenicSpot {
        fun byCity(city: City): String {
            return city.value.let {
                "City eq '$it' or startswith(Address, '$it')"
            }
        }

        fun byZipCode(zipCode: Int): String {
            return "ZipCode eq '$zipCode'"
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