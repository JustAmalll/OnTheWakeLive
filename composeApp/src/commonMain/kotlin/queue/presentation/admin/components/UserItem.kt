package queue.presentation.admin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.components.UserPhoto
import core.presentation.ui.theme.gradientBackground
import core.presentation.utils.clickableWithoutIndication

@Composable
fun UserItem(
    firstName: String,
    lastName: String?,
    photo: String?,
    onItemClicked: () -> Unit = {},
    onPhotoClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clickableWithoutIndication(onClick = onItemClicked)
            .gradientBackground(radius = 16.dp)
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserPhoto(photo = photo, onClick = onPhotoClicked)

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = firstName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            lastName?.let {
                Text(
                    text = lastName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}