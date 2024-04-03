package core.presentation.utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? = remember(bytes) {
    if (bytes == null) return@remember null

    try {
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
    } catch (exception: Exception) {
        null
    }
}