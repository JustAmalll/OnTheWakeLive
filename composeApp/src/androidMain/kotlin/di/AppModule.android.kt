package di

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import core.presentation.utils.OpenTelegramUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.provideObservableSettings(): ObservableSettings = SharedPreferencesSettings(
    androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
)

actual fun Scope.provideOpenTelegramUtil() = OpenTelegramUtil(context = androidContext())