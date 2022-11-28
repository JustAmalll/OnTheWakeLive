package com.onthewake.onthewakelive

import android.app.Application
import com.qonversion.android.sdk.Qonversion
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnTheWakeLiveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Qonversion.setDebugMode()
        Qonversion.launch(
            context = this,
            key = "9p55IrPEEYXrNbI6rnsPwSZ73o6xnBCz",
            observeMode = false
        )
    }
}