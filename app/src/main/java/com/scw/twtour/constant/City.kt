package com.scw.twtour.constant

import android.util.Log
import timber.log.Timber

enum class City(val value: String) {
    ALL("全部地區"),
    TAIPEI("臺北市"),
    NEW_TAIPEI("新北市"),
    TAOYUAN("桃園市"),
    TAICHUNG("臺中市"),
    TAINAN("臺南市"),
    KAOHSIUNG("高雄市"),
    KEELUNG("基隆市"),
    HSINCHU("新竹市"),
    HSINCHU_COUNTRY("新竹縣"),
    MIAOLI_COUNTRY("苗栗縣"),
    CHANGHUA_COUNTRY("彰化縣"),
    NANTOU_COUNTRY("南投縣"),
    YUNLIN_COUNTRY("雲林縣"),
    CHIAYI_COUNTRY("嘉義縣"),
    CHIAYI("嘉義市"),
    PINGTUNG_COUNTRY("屏東縣"),
    YILAN_COUNTRY("宜蘭縣"),
    HUALIEN_COUNTRY("花蓮縣"),
    TAITUNG_COUNTRY("臺東縣"),
    KINMEN_COUNTRY("金門縣"),
    PENGHU_COUNTRY("澎湖縣"),
    LIENCHIANG_COUNTRY("連江縣"),

    // 區域
    LANYU("蘭嶼"),
    LYUDAO("綠島"),
    XIAOLIOUCHOU("小琉球");

    companion object {
        fun fromValue(value: String): City? {
            return try {
                values().first { it.value == value }
            } catch (e: Exception) {
                Timber.e(Log.getStackTraceString(e))
                null
            }
        }
    }
}