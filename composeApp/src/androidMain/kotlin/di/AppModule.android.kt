package di

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.provideObservableSettings(): ObservableSettings = SharedPreferencesSettings(
    androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
)