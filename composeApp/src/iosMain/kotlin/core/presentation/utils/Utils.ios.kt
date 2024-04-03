package core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

@Composable
actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? = remember(bytes) {
    if (bytes == null) return@remember null

    try {
        Bitmap.makeFromImage(image = Image.makeFromEncoded(bytes = bytes)).asComposeImageBitmap()
    } catch (exception: Exception) {
        null
    }
}