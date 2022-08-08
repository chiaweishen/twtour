package com.scw.twtour.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor(private val accessToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}