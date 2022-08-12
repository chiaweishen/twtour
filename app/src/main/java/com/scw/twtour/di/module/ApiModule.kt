package com.scw.twtour.di.module

import com.scw.twtour.network.AuthApiService
import com.scw.twtour.network.BasicApiService
import org.koin.dsl.module

val apiModule = module {
    single { AuthApiService() }
    single { BasicApiService() }
    single { get<AuthApiService>().authApi() }
    single { get<BasicApiService>().tourismApi() }
}