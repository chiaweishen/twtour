package com.scw.twtour.di.module

import com.scw.twtour.view.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ScenicSpotListViewModel(get(), get()) }
    viewModel { ScenicSpotNoteListViewModel(get(), get()) }
    viewModel { ScenicSpotDetailsViewModel(get(), get()) }
}