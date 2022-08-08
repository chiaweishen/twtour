package com.scw.twtour.http

import com.scw.twtour.http.adapter.FlowCallAdapterFactory
import com.scw.twtour.http.api.TourismApi
import com.scw.twtour.http.interceptor.ApiInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasicApiService {

    private var accessToken: String = ""

    companion object {
        private const val BASE_URL = "https://tdx.transportdata.tw/api/basic/"
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(ApiInterceptor(accessToken))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun tourismApi(): TourismApi = retrofit.create(TourismApi::class.java)
}