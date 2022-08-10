package com.scw.twtour.di.module

import com.scw.twtour.db.BasicDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbTestModule = module {
    single { BasicDatabase.getTestingInstance(androidContext()) }
    single { get<BasicDatabase>().scenicSpotDao() }
}