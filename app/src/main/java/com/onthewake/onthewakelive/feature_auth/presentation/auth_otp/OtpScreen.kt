package com.onthewake.onthewakelive.feature_auth.presentation.auth_otp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.StandardTextField
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen

@ExperimentalMaterial3Api
@Composable
fun OtpScreen(
    navController: NavHostController,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    snackBarHostState.showSnackbar(
                        message = "Registration complete",
                        duration = SnackbarDuration.Short
                    )
                }
                is AuthResult.IncorrectOtp -> {
                    snackBarHostState.showSnackbar(
                        message = "IncorrectOtp",
                        duration = SnackbarDuration.Short
                    )
                }
                else -> {
                    snackBarHostState.showSnackbar(
                        message = context.getString(R.string.unknown_error),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
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
                    text = stringResource(id = R.string.verify_phone_number),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.otp,
                    onValueChange = {
                        if (it.length <= 6) {
                            viewModel.onEvent(OtpUiEvent.OtpCodeChanged(it))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    label = "Code",
                    isError = state.otpError != null,
                    errorText = state.otpError,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(OtpUiEvent.VerifyOtpAndSignUp)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Verify")
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
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}