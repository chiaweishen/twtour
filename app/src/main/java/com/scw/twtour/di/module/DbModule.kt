package com.scw.twtour.di.module

import com.scw.twtour.db.BasicDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { BasicDatabase.getInstance(androidContext()) }
    single { get<BasicDatabase>().scenicSpotDao() }
    single { get<BasicDatabase>().noteDao() }
}