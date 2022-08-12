package com.scw.twtour.di.module

import com.scw.twtour.model.repository.ScenicSpotRepository
import com.scw.twtour.model.repository.ScenicSpotRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ScenicSpotRepository> { ScenicSpotRepositoryImpl(get(), get(), get()) }
}