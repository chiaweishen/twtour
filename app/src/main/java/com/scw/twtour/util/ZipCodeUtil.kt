package com.scw.twtour.util

import android.content.Context
import com.scw.twtour.R
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber


class ZipCodeUtil(context: Context) {
    private val zipCodeMap = mutableMapOf<Int, String>()

    companion object {
        fun getOutlingIslandsZipCode(city: City): Int {
            return when (city) {
                City.LANYU -> 952
                City.LYUDAO -> 951
                City.XIAOLIOUCHOU -> 929
                else -> 0
            }
        }
    }

    init {
        try {
            val jsonString = context.resources
                .openRawResource(R.raw.taiwan_districts)
                .bufferedReader()
                .use { it.readText() }

            JSONArray(jsonString).also { array ->
                (0 until array.length()).forEach {
                    val jsonObject: JSONObject = array[it] as JSONObject
                    val districts = jsonObject.getJSONArray("districts")
                    (0 until districts.length()).forEach { index ->
                        val district: JSONObject = districts[index] as JSONObject
                        zipCodeMap[district.getInt("zip")] = district.getString("name")
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getZipCodeName(zipCode: Int?): String {
        return zipCodeMap[zipCode] ?: ""
    }

}