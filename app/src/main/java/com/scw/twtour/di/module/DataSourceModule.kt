package com.scw.twtour.di.module

import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotLocalDataSourceImpl
import com.scw.twtour.model.datasource.local.ScenicSpotPreferencesDataSource
import com.scw.twtour.model.datasource.local.ScenicSpotPreferencesDataSourceImpl
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSource
import com.scw.twtour.model.datasource.remote.ScenicSpotRemoteDataSourceImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceModule = module {
    single<ScenicSpotRemoteDataSource> { ScenicSpotRemoteDataSourceImp(get()) }
    single<ScenicSpotLocalDataSource> { ScenicSpotLocalDataSourceImpl(get()) }
    single<ScenicSpotPreferencesDataSource> { ScenicSpotPreferencesDataSourceImpl(androidContext()) }
}