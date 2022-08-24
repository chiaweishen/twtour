package com.scw.twtour.util

object CityUtil {
    fun parseAddressToCity(address: String?): City? {
        return address?.let {
            if (it.startsWith("臺北市")) {
                City.TAIPEI
            } else if (it.startsWith("新北市")) {
                City.NEW_TAIPEI
            } else if (it.startsWith("桃園市")) {
                City.TAOYUAN
            } else if (it.startsWith("臺中市")) {
                City.TAICHUNG
            } else if (it.startsWith("臺南市")) {
                City.TAINAN
            } else if (it.startsWith("高雄市")) {
                City.KAOHSIUNG
            } else if (it.startsWith("基隆市")) {
                City.KEELUNG
            } else if (it.startsWith("新竹市")) {
                City.HSINCHU
            } else if (it.startsWith("新竹縣")) {
                City.HSINCHU_COUNTRY
            } else if (it.startsWith("苗栗縣")) {
                City.MIAOLI_COUNTRY
            } else if (it.startsWith("彰化縣")) {
                City.CHANGHUA_COUNTRY
            } else if (it.startsWith("南投縣")) {
                City.NANTOU_COUNTRY
            } else if (it.startsWith("雲林縣")) {
                City.YUNLIN_COUNTRY
            } else if (it.startsWith("嘉義縣")) {
                City.CHIAYI_COUNTRY
            } else if (it.startsWith("嘉義市")) {
                City.CHIAYI
            } else if (it.startsWith("屏東縣")) {
                City.PINGTUNG_COUNTRY
            } else if (it.startsWith("宜蘭縣")) {
                City.YILAN_COUNTRY
            } else if (it.startsWith("花蓮縣")) {
                City.HUALIEN_COUNTRY
            } else if (it.startsWith("臺東縣")) {
                City.TAITUNG_COUNTRY
            } else if (it.startsWith("金門縣")) {
                City.KINMEN_COUNTRY
            } else if (it.startsWith("澎湖縣")) {
                City.PENGHU_COUNTRY
            } else if (it.startsWith("連江縣")) {
                City.LIENCHIANG_COUNTRY
            } else if (it.startsWith("蘭嶼")) {
                City.LANYU
            } else if (it.startsWith("綠島")) {
                City.LYUDAO
            } else if (it.startsWith("小琉球")) {
                City.XIAOLIOUCHOU
            } else {
                null
            }
        }
    }
}