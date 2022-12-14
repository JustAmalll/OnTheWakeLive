package com.onthewake.onthewakelive.feature_auth.presentation.auth_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.StandardTextField
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.core.util.toJson
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.registerResults) {
        viewModel.registerResults.collect { result ->
            when (result) {
                AuthResult.OtpTooManyRequests -> snackBarHostState.showSnackbar(
                    message = "OtpTooManyRequests"
                )
                AuthResult.OtpInvalidCredentials -> snackBarHostState.showSnackbar(
                    message = "Invalid phone number format"
                )
                AuthResult.UserAlreadyExist -> snackBarHostState.showSnackbar(
                    message = "User with this phone number already exists"
                )
                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.unknown_error)
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.navigateUpEvent.collectLatest { registerData ->
            navController.navigate(
                Screen.OtpScreen.passRegisterData(
                    RegisterData(
                        firstName = registerData.firstName,
                        lastName = registerData.lastName,
                        phoneNumber = registerData.phoneNumber,
                        password = registerData.password
                    ).toJson()
                )
            )
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signUpFirstName,
                    onValueChange = {
                        viewModel.onEvent(RegisterUiEvent.SignUpFirstNameChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.first_name),
                    isError = state.signUpFirstNameError != null,
                    errorText = state.signUpFirstNameError
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signUpLastName,
                    onValueChange = {
                        viewModel.onEvent(RegisterUiEvent.SignUpLastNameChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.last_name),
                    isError = state.signUpLastNameError != null,
                    errorText = state.signUpLastNameError
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signUpPhoneNumber,
                    onValueChange = {
                        viewModel.onEvent(RegisterUiEvent.SignUpPhoneNumberChanged(it))
                    },
                    label = stringResource(id = R.string.phone_number),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.signUpPhoneNumberError != null,
                    errorText = state.signUpPhoneNumberError
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signUpPassword,
                    onValueChange = {
                        viewModel.onEvent(RegisterUiEvent.SignUpPasswordChanged(it))
                    },
                    label = stringResource(id = R.string.password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(RegisterUiEvent.SendOtp(context))
                            focusManager.clearFocus()
                        }
                    ),
                    isPasswordTextField = true,
                    isError = state.signUpPasswordError != null,
                    errorText = state.signUpPasswordError
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(RegisterUiEvent.SendOtp(context))
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(id = R.string.sign_up))
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable { navController.navigate(Screen.LoginScreen.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_an_account),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}