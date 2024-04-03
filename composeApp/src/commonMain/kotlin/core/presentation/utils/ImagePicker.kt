package core.presentation.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImagePicked: (ByteArray) -> Unit): ImagePickerLauncher

expect class ImagePickerLauncher(onLaunch: () -> Unit) {
    fun launch()
}