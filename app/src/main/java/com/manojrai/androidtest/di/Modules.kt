package com.manojrai.androidtest.di

import com.manojrai.androidtest.data.local.DbHelper
import com.manojrai.androidtest.ui.addnew.AddNewViewModel
import com.manojrai.androidtest.ui.main.MainViewModel
import com.manojrai.androidtest.utils.rx.RxSchedulerProvider
import com.manojrai.androidtest.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    factory<SchedulerProvider> { RxSchedulerProvider() }

    factory { CompositeDisposable() }

    single {
        DbHelper()
    }
}

val mainModule = module {
    viewModel {
        MainViewModel(compositeDisposable = get(), schedulerProvider = get(), dbHelper = get())
    }
}

val addNewModule = module {
    viewModel {
        AddNewViewModel(compositeDisposable = get(), schedulerProvider = get(), dbHelper = get())
    }
}