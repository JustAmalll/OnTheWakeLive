package com.onthewake.onthewakelive

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.onesignal.OneSignal
import com.onthewake.onthewakelive.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnTheWakeLiveApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private const val ONESIGNAL_APP_ID = "8890bfbd-a1d0-426a-98ae-88aa5b954b68"
    }
}