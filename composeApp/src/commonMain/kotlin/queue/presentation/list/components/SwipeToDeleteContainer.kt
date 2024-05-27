package queue.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    swipeEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            false
        }
    )

    if (swipeEnabled) {
        SwipeToDismissBox(
            state = state,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = swipeEnabled,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 1.dp)
                        .padding(all = 1.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFF0000), Color(0xFFED4343)),
                            ),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = stringResource(Res.string.delete),
                        fontSize = 16.sp
                    )
                }
            },
            content = { content() }
        )
    } else {
        content()
    }
}