package com.scw.twtour.di.module

import com.scw.twtour.view.viewmodel.HomeViewModel
import com.scw.twtour.view.viewmodel.MainViewModel
import com.scw.twtour.view.viewmodel.ScenicSpotDetailsViewModel
import com.scw.twtour.view.viewmodel.ScenicSpotListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { params -> MainViewModel(params.get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ScenicSpotListViewModel(get(), get()) }
    viewModel { ScenicSpotDetailsViewModel(get(), get()) }
}