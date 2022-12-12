package com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.StandardTextField
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue

@ExperimentalMaterial3Api
@Composable
fun AdminDialog(
    showDialog: (Boolean) -> Unit,
    onAddClicked: (Boolean, String) -> Unit,
    queue: List<Queue>
) {

    var leftButtonState by remember { mutableStateOf(false) }
    var rightButtonState by remember { mutableStateOf(true) }
    var firstNameFieldState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val rightButtonColor = if (rightButtonState) MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.colorScheme.onPrimary
    val leftButtonColor = if (leftButtonState) MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.colorScheme.onPrimary

    val context = LocalContext.current

    Dialog(onDismissRequest = { showDialog(false) }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Box(
                modifier = Modifier
                    .padding(14.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.add_to_queue),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                leftButtonState = !leftButtonState
                                rightButtonState = !rightButtonState
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = rightButtonColor, contentColor = rightButtonColor
                            )
                        ) {
                            Text(text = stringResource(id = R.string.left_line_admin))
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            onClick = {
                                leftButtonState = !leftButtonState
                                rightButtonState = !rightButtonState
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = leftButtonColor, contentColor = leftButtonColor
                            )
                        ) {
                            Text(text = stringResource(id = R.string.right_line_admin))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    StandardTextField(
                        value = firstNameFieldState,
                        onValueChange = { firstNameFieldState = it },
                        label = stringResource(id = R.string.first_name),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isError = errorMessage.isNotEmpty(),
                        errorText = errorMessage
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val addToQueueResult = ValidationUseCase(context = context)
                                .validateAdminAddToQueue(
                                    firstName = firstNameFieldState, queue = queue
                                )
                            if (addToQueueResult.errorMessage != null) {
                                errorMessage = addToQueueResult.errorMessage
                            }
                            if (addToQueueResult.successful) {
                                onAddClicked(leftButtonState, firstNameFieldState)
                                showDialog(false)
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.add))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}