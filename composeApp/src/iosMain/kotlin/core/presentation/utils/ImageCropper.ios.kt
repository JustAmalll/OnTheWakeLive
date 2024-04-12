package core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun ImageCropper(photo: ByteArray, onImageCropped: (ByteArray) -> Unit) {
    LaunchedEffect(key1 = photo) {
        onImageCropped(photo)
    }
}