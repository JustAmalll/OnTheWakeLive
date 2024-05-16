package queue.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.components.UserPhoto
import core.presentation.utils.clickableWithoutIndication
import sh.calvin.reorderable.ReorderableItemScope

@Composable
fun ReorderableItemScope.QueueItem(
    firstName: String,
    lastName: String?,
    photo: String?,
    showDraggableHandle: Boolean,
    onItemClicked: () -> Unit,
    onPhotoClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clickableWithoutIndication(onClick = onItemClicked)
            .background(
                color = Color(0xFF424045),
                shape = RoundedCornerShape(size = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 1.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF27252A), Color(0xFF302E33)),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    ),
                    shape = RoundedCornerShape(size = 16.dp)
                )
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
            if (showDraggableHandle) {
                Spacer(modifier = Modifier.weight(1f))

                IconButton(modifier = Modifier.draggableHandle(), onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.DragHandle,
                        contentDescription = null
                    )
                }
            }
        }
    }
}