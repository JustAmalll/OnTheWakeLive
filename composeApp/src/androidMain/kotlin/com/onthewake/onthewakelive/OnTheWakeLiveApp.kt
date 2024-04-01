package com.onthewake.onthewakelive

import android.app.Application
import di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OnTheWakeLiveApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OnTheWakeLiveApp)
            androidLogger()
            modules(appModules())
        }
    }
}