package com.onthewake.onthewakelive

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.onesignal.OneSignal
import com.onthewake.onthewakelive.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnTheWakeLiveApp : Application()