package core.presentation.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import com.smarttoolfactory.cropper.settings.CropType
import java.io.ByteArrayOutputStream

@Composable
actual fun ImageCropper(
    photo: ByteArray,
    onImageCropped: (ByteArray) -> Unit
) {
    var bitmap by remember(photo) {
        mutableStateOf(photo.toBitmap())
    }
    val cropProperties by remember {
        mutableStateOf(
            CropDefaults.properties(
                cropOutlineProperty = CropOutlineProperty(
                    OutlineType.Rect,
                    RectCropShape(0, "Rect")
                ),
                cropType = CropType.Static,
                handleSize = 0f,
                maxZoom = 5f,
                aspectRatio = AspectRatio(1f)
            )
        )
    }
    val cropStyle by remember { mutableStateOf(CropDefaults.style()) }
    var crop by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { bitmap = bitmap?.rotate() }) {
                        Icon(
                            imageVector = Icons.Filled.Rotate90DegreesCw,
                            contentDescription = null
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { crop = true },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(imageVector = Icons.Filled.Crop, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.DarkGray)
        ) {
            bitmap?.let {
                ImageCropper(
                    modifier = Modifier.fillMaxSize(),
                    imageBitmap = it,
                    contentDescription = null,
                    cropStyle = cropStyle,
                    cropProperties = cropProperties,
                    crop = crop,
                    onCropStart = {},
                    onCropSuccess = { image ->
                        onImageCropped(image.toByteArray())
                        crop = false
                    }
                )
            }
        }
    }
}

private fun ByteArray.toBitmap(): ImageBitmap? = try {
    BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
} catch (exception: Exception) {
    null
}

private fun ImageBitmap.rotate(): ImageBitmap {
    val bitmap = asAndroidBitmap()
    val matrix = Matrix()
    matrix.postRotate(90f)

    return Bitmap
        .createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        .asImageBitmap()
}

private fun ImageBitmap.toByteArray(): ByteArray = ByteArrayOutputStream().use { stream ->
    asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, 70, stream)
    return stream.toByteArray()
}
