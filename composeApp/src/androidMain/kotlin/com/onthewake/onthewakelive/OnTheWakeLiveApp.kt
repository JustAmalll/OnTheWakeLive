package com.onthewake.onthewakelive

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OnTheWakeLiveApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OnTheWakeLiveApp)
            androidLogger()
            modules(appModules())
        }
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.mipmap.ic_launcher_foreground
            )
        )
    }
}