package com.scw.twtour.di.module

import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.domain.AuthUseCaseImpl
import com.scw.twtour.domain.SyncScenicSpotUseCase
import com.scw.twtour.domain.SyncScenicSpotUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<AuthUseCase> { params -> AuthUseCaseImpl(get(), params[0], params[1]) }
    single<SyncScenicSpotUseCase> { SyncScenicSpotUseCaseImpl(get()) }
}