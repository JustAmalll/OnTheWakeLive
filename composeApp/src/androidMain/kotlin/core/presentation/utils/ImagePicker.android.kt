package core.presentation.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (ByteArray) -> Unit): ImagePickerLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.openInputStream(uri)?.use {
                    onImagePicked(it.readBytes())
                }
            }
        }
    )

    return remember {
        ImagePickerLauncher {
            launcher.launch(PickVisualMediaRequest(ImageOnly))
        }
    }
}

actual class ImagePickerLauncher actual constructor(private val onLaunch: () -> Unit) {

    actual fun launch() {
        onLaunch()
    }
}