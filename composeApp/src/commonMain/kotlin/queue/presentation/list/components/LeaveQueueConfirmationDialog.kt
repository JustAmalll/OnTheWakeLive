package queue.presentation.list.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.admin_remove_user_from_queue_confirmation
import onthewakelive.composeapp.generated.resources.cancel
import onthewakelive.composeapp.generated.resources.confirm_action
import onthewakelive.composeapp.generated.resources.leave
import onthewakelive.composeapp.generated.resources.leave_queue_confirmation
import onthewakelive.composeapp.generated.resources.remove
import onthewakelive.composeapp.generated.resources.stay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LeaveQueueConfirmationDialog(
    onDismissRequest: () -> Unit,
    isUserAdmin: Boolean,
    onLeaveQueue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.confirm_action)) },
        text = {
            Text(
                text = stringResource(
                    if (isUserAdmin) {
                        Res.string.admin_remove_user_from_queue_confirmation
                    } else {
                        Res.string.leave_queue_confirmation
                    }
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onLeaveQueue) {
                Text(
                    text = stringResource(
                        if (isUserAdmin) {
                            Res.string.remove
                        } else {
                            Res.string.leave
                        }
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = stringResource(
                        if (isUserAdmin) {
                            Res.string.cancel
                        } else {
                            Res.string.stay
                        }
                    )
                )
            }
        }
    )
}