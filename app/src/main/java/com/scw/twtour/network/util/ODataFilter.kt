package com.scw.twtour.network.util

import com.scw.twtour.util.City

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
                    "or ${lyudao()} " +
                    "or ${lanyu()} " +
                    "or ${xiaoliouchou()}"
        }

        // 綠島
        fun lyudao(): String {
            return "ZipCode eq '951'"
        }

        // 蘭嶼
        fun lanyu(): String {
            return "ZipCode eq '952'"
        }

        // 小琉球
        fun xiaoliouchou(): String {
            return "ZipCode eq '929'"
        }
    }
}