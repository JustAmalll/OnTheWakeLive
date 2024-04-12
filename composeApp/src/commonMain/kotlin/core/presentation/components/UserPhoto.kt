package core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import core.utils.Constants

@Composable
fun UserPhoto(
    photo: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        if (photo != null) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onClick),
                model = "${Constants.BASE_URL}/storage/$photo",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
    }
}