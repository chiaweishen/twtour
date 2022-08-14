package com.scw.twtour.di.module

import com.scw.twtour.view.viewmodel.HomeViewModel
import com.scw.twtour.view.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { params -> MainViewModel(params.get(), get()) }
    viewModel { HomeViewModel(get()) }
}