package com.mj.board.application

import android.app.Application
import com.mj.board.viewmodel.AddViewModel
import com.mj.board.viewmodel.DetailViewModel
import com.mj.board.viewmodel.MainViewModel
import com.mj.board.viewmodel.MemoFragViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(listOf(mainViewModelModule, addViewModelModule, detailViewModelModule, memoFragViewModel))
        }
    }

    val mainViewModelModule = module {
        viewModel { MainViewModel(androidApplication()) }
    }

    val addViewModelModule = module {

        viewModel { AddViewModel(androidApplication()) }
    }

    val detailViewModelModule = module {
        viewModel { DetailViewModel(androidApplication()) }
    }

    val memoFragViewModel = module {
        viewModel { MemoFragViewModel(androidApplication()) }
    }
}