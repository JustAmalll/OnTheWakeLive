package core.presentation.utils

import androidx.compose.runtime.Composable

@Composable
expect fun ImageCropper(
    photo: ByteArray,
    onImageCropped: (ByteArray) -> Unit
)