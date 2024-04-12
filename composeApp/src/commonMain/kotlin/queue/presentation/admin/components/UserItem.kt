package queue.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.components.UserPhoto

@Composable
fun UserItem(
    firstName: String,
    lastName: String?,
    photo: String?,
    onItemClicked: () -> Unit = {},
    onPhotoClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserPhoto(photo = photo, onClick = onPhotoClicked)

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = firstName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            lastName?.let {
                Text(
                    text = lastName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}