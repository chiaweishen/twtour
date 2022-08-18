package com.scw.twtour.util

import android.content.Context
import com.scw.twtour.R
import org.json.JSONArray
import org.json.JSONObject


class ZipCodeUtil(context: Context) {
    private val zipCodeMap = mutableMapOf<Int, String>()

    init {
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
    }

    fun getZipCodeName(zipCode: Int?): String {
        return zipCodeMap[zipCode] ?: ""
    }

}