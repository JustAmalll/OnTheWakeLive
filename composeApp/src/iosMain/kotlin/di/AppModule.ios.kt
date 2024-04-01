package di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.core.scope.Scope
import platform.Foundation.NSUserDefaults

actual fun Scope.provideObservableSettings(): ObservableSettings = NSUserDefaultsSettings(
    delegate = NSUserDefaults.standardUserDefaults
)