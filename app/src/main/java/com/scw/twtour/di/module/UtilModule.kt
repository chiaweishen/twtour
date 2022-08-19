package com.scw.twtour.di.module

import com.scw.twtour.util.ZipCodeUtil
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilModule = module {
    single { ZipCodeUtil(androidContext()) }
}