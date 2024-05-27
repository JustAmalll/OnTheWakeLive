package queue.presentation.list.components

import LocalToggleBackgroundBlur
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import core.presentation.ui.theme.gradientBackground
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
    val localToggleBackgroundBlur = LocalToggleBackgroundBlur.current

    val dismissDialog = remember {
        {
            localToggleBackgroundBlur()
            onDismissRequest()
        }
    }
    LaunchedEffect(true) {
        localToggleBackgroundBlur()
    }

    Dialog(onDismissRequest = dismissDialog) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gradientBackground(radius = 16.dp)
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.confirm_action),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = stringResource(
                    if (isUserAdmin) {
                        Res.string.admin_remove_user_from_queue_confirmation
                    } else {
                        Res.string.leave_queue_confirmation
                    }
                ),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = dismissDialog,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(
                            if (isUserAdmin) {
                                Res.string.cancel
                            } else {
                                Res.string.stay
                            }
                        ),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        localToggleBackgroundBlur()
                        onLeaveQueue()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(
                            if (isUserAdmin) {
                                Res.string.remove
                            } else {
                                Res.string.leave
                            }
                        ),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}