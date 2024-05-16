package di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import core.presentation.utils.OpenTelegramUtil
import org.koin.core.scope.Scope
import platform.Foundation.NSUserDefaults

actual fun Scope.provideObservableSettings(): ObservableSettings = NSUserDefaultsSettings(
    delegate = NSUserDefaults.standardUserDefaults
)

actual fun Scope.provideOpenTelegramUtil() = OpenTelegramUtil()