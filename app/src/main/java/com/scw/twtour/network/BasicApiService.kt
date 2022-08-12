package com.scw.twtour.network

import com.scw.twtour.network.adapter.FlowCallAdapterFactory
import com.scw.twtour.network.api.TourismApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BasicApiService {

    companion object {
        private const val BASE_URL = "https://tdx.transportdata.tw/api/basic/"
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
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