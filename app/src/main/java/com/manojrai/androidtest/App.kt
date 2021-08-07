package com.manojrai.androidtest

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.manojrai.androidtest.di.addNewModule
import com.manojrai.androidtest.di.appModule
import com.manojrai.androidtest.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        private lateinit var app: App

        fun getInstance(): App {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            koin.loadModules(
                listOf(
                    appModule,
                    mainModule,
                    addNewModule
                )
            )
            //koin.createRootScope()
        }
    }

    fun getAppContext(): Context {
        return applicationContext
    }
}