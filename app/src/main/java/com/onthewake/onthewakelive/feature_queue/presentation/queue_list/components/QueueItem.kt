package com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.util.Constants
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun QueueItem(
    queueItem: Queue,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit,
    onUserAvatarClicked: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isAdminQueueItem = queueItem.userId in Constants.ADMIN_IDS

    val swipeToDelete = SwipeAction(
        icon = {
            Icon(
                modifier = Modifier.padding(end = 20.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_icon),
                tint = MaterialTheme.colorScheme.onError
            )
        },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
            onSwipeToDelete(queueItem.id)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    )

    Spacer(modifier = Modifier.height(12.dp))

    if (queueItem.userId == userId || userId in Constants.ADMIN_IDS) {
        SwipeableActionsBox(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { if (!isAdminQueueItem) onDetailsClicked(queueItem.id) },
            startActions = listOf(swipeToDelete),
            swipeThreshold = 100.dp
        ) {
            if (isAdminQueueItem) QueueItemAddedByAdmin(firstName = queueItem.firstName)
            else QueueItemContent(
                queueItem = queueItem,
                imageLoader = imageLoader,
                onDetailsClicked = onDetailsClicked,
                onUserAvatarClicked = onUserAvatarClicked
            )
        }
    } else {
        if (isAdminQueueItem) QueueItemAddedByAdmin(firstName = queueItem.firstName)
        else QueueItemContent(
            queueItem = queueItem,
            imageLoader = imageLoader,
            onDetailsClicked = onDetailsClicked,
            onUserAvatarClicked = onUserAvatarClicked
        )
    }
}