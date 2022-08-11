package com.scw.twtour.network.util

object HeadersProvider {

    private var accessToken: String = ""

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun get(): Map<String, String> {
        return hashMapOf<String, String>().apply {
            put("authorization", "Bearer $accessToken")
        }
    }

}