package com.onthewake.onthewakelive.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
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

fun Context.openInstagramProfile(instagram: String) {
    this.startActivity(
        Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "${Constants.INSTAGRAM_URL}/$instagram/"
            )
        )
    )
}