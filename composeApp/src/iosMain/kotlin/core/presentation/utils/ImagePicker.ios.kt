package core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (ByteArray) -> Unit): ImagePickerLauncher {
    val imagePickerController = UIImagePickerController().apply {
        sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
    }

    val delegate = object : NSObject(), UIImagePickerControllerDelegateProtocol,
        UINavigationControllerDelegateProtocol {

        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingImage: UIImage,
            editingInfo: Map<Any?, *>?
        ) {
            val imageNsData = UIImageJPEGRepresentation(
                image = didFinishPickingImage,
                compressionQuality = 1.0
            ) ?: return

            val bytes = ByteArray(size = imageNsData.length.toInt())
            memcpy(bytes.refTo(0), imageNsData.bytes, imageNsData.length)

            onImagePicked(bytes)
            picker.dismissViewControllerAnimated(true, null)
        }

        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            picker.dismissViewControllerAnimated(true, null)
        }
    }

    return remember {
        ImagePickerLauncher {
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                viewControllerToPresent = imagePickerController,
                animated = true
            ) {
                imagePickerController.delegate = delegate
            }
        }
    }
}

actual class ImagePickerLauncher actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}