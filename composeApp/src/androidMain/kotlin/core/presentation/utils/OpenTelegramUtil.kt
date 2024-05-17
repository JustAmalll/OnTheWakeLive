package core.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class OpenTelegramUtil(private val context: Context) {

    actual fun open(telegramId: String) {
        try {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$telegramId"))
            telegram.setPackage("org.telegram.messenger")
            context.startActivity(telegram)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}