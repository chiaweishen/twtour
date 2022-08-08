package com.scw.twtour.http.util

import com.scw.twtour.http.data.City

object ODataFilter {
    object ScenicSpot {
        fun byCity(city: City): String {
            return city.value.let {
                "City eq '$it' or startswith(Address, '$it')"
            }
        }

        fun byId(id: String): String {
            return "ScenicSpotID eq '$id'"
        }

        fun outlyingIslands(): String {
            return "${byCity(City.PENGHU_COUNTRY)} " +
                    "or ${byCity(City.KINMEN_COUNTRY)} " +
                    "or ZipCode eq '951' " + // 綠島
                    "or ZipCode eq '952' " + // 蘭嶼
                    "or ZipCode eq '929'" // 小琉球
        }
    }
}