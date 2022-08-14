package com.scw.twtour.di.module

import com.scw.twtour.domain.*
import org.koin.dsl.module

val useCaseModule = module {
    single<AuthUseCase> { params -> AuthUseCaseImpl(get(), params[0], params[1]) }
    single<SyncScenicSpotUseCase> { SyncScenicSpotUseCaseImpl(get()) }
    single<ScenicSpotUseCase> { ScenicSpotUseCaseImpl(get()) }
}