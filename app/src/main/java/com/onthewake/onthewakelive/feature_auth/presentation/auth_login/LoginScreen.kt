package com.onthewake.onthewakelive.feature_auth.presentation.auth_login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.components.StandardLoadingView
import com.onthewake.onthewakelive.core.presentation.StandardTextField
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val haptic = LocalHapticFeedback.current
    val systemBarsColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarsColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(viewModel, context) {
        viewModel.loginResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> navController.navigate(Screen.QueueScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
                is AuthResult.IncorrectData -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.incorrect_data)
                )
                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.unknown_error)
                )
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signInPhoneNumber,
                    onValueChange = {
                        viewModel.onEvent(LoginUiEvent.SignInPhoneNumberChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.phone_number),
                    isError = state.signInPhoneNumberError != null,
                    errorText = state.signInPhoneNumberError,
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.signInPassword,
                    onValueChange = {
                        viewModel.onEvent(LoginUiEvent.SignInPasswordChanged(it))
                    },
                    label = stringResource(id = R.string.password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(LoginUiEvent.SignIn)
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            focusManager.clearFocus()
                        }
                    ),
                    isError = state.signInPasswordError != null,
                    errorText = state.signInPasswordError,
                    isPasswordTextField = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(LoginUiEvent.SignIn)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 12.dp)
                    .clickable {
                        navController.navigate(Screen.RegisterScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_an_account_yet),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stringResource(id = R.string.sign_up)}!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    if (state.isLoading) StandardLoadingView()
}