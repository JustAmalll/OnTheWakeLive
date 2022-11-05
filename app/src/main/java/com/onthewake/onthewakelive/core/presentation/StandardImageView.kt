package com.onthewake.onthewakelive.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.onthewake.onthewakelive.R

@Composable
fun StandardImageView(
    imageLoader: ImageLoader,
    model: String
) {
    val isImageLoading = remember { mutableStateOf(false) }

    IconButton(onClick = {}, modifier = Modifier.size(46.dp)) {
        if (!isImageLoading.value) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(id = R.string.person_icon)
            )
        }
        if (isImageLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(26.dp), strokeWidth = 2.dp)
        }
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            painter = rememberAsyncImagePainter(
                model = model,
                imageLoader = imageLoader,
                onLoading = { isImageLoading.value = true },
                onError = { isImageLoading.value = false },
                onSuccess = { isImageLoading.value = false }
            ),
            contentDescription = stringResource(id = R.string.user_picture)
        )
    }
}