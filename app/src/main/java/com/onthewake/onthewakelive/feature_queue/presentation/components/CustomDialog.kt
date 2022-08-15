package com.onthewake.onthewakelive.feature_queue.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.DefaultTextField
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.ui.theme.ItemBgColor
import com.onthewake.onthewakelive.ui.theme.Primary

@Composable
fun CustomDialog(
    showDialog: (Boolean) -> Unit,
    onAddClicked: (Boolean, String) -> Unit,
    queue: List<Queue>
) {

    val leftQueueButtonState = remember { mutableStateOf(true) }
    val rightQueueButtonState = remember { mutableStateOf(false) }

    val firstNameFieldState = remember { mutableStateOf("") }

    val errorMessage = remember { mutableStateOf("") }

    val darkTheme = isSystemInDarkTheme()

    val context = LocalContext.current

    Dialog(onDismissRequest = { showDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.ItemBgColor
        ) {
            Box(
                modifier = Modifier.padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.add_to_queue),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (darkTheme) Color.White else Color.Black,
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
                                leftQueueButtonState.value = true
                                rightQueueButtonState.value = false
                            }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (leftQueueButtonState.value) Primary
                                else Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = stringResource(id = R.string.left_line_admin))
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            onClick = {
                                leftQueueButtonState.value = false
                                rightQueueButtonState.value = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (rightQueueButtonState.value) Primary
                                else Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = stringResource(id = R.string.right_line_admin))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    DefaultTextField(
                        value = firstNameFieldState.value,
                        onValueChange = { firstNameFieldState.value = it },
                        label = stringResource(id = R.string.first_name),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isError = errorMessage.value.isNotEmpty(),
                        errorText = errorMessage.value
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val addToQueueResult = ValidationUseCase(context = context)
                                .validateAdminAddToQueue(
                                    firstName = firstNameFieldState.value,
                                    queue = queue
                                )

                            if (addToQueueResult.errorMessage != null) {
                                errorMessage.value = addToQueueResult.errorMessage
                            }

                            if (addToQueueResult.successful) {
                                onAddClicked(leftQueueButtonState.value, firstNameFieldState.value)
                                showDialog(false)
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.add), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}