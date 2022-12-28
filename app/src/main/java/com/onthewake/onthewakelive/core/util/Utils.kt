package com.onthewake.onthewakelive.core.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.gson.Gson

fun <T> T.toJson(): String = Gson().toJson(this)
fun <T> String.fromJson(type: Class<T>): T = Gson().fromJson(this, type)

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

fun Context.openInstagramProfile(instagram: String) = this.startActivity(
    Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.INSTAGRAM_URL}/$instagram/"))
)

fun Context.openNotificationSettings() = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    putExtra("android.provider.extra.APP_PACKAGE", this@openNotificationSettings.packageName)
    this@openNotificationSettings.startActivity(this)
}
