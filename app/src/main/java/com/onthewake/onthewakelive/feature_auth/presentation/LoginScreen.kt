package com.onthewake.onthewakelive.feature_auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.onthewake.onthewakelive.core.presentation.DefaultTextField
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.ui.theme.BackgroundColor
import com.onthewake.onthewakelive.ui.theme.Primary
import com.onthewake.onthewakelive.ui.theme.darkThemeBgColor

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
) {
    val state = viewModel.state
    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = if (darkTheme) Color.Black else Primary, darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = if (darkTheme) darkThemeBgColor else Color.White,
            darkIcons = !darkTheme
        )
    }

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    navController.navigate(Screen.QueueScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        popUpTo(Screen.RegisterScreen.route) { inclusive = true }
                    }
                }
                is AuthResult.IncorrectData -> {
                    showSnackBar(context.getString(R.string.incorrect_data))
                }
                is AuthResult.UnknownError -> {
                    showSnackBar(context.getString(R.string.unknown_error))
                }
                else -> {
                    showSnackBar(context.getString(R.string.unknown_error))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.BackgroundColor)
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
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signInPhoneNumber,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignInPhoneNumberChanged(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(id = R.string.phone_number),
                isError = state.signUpPhoneNumberError != null,
                errorText = state.signUpPhoneNumber,
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signInPassword,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it))
                },
                label = stringResource(id = R.string.password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = state.signInPasswordError != null,
                errorText = state.signInPasswordError,
                isPasswordTextField = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onEvent(AuthUiEvent.SignIn)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    color = Color.White
                )
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
            Text(text = stringResource(id = R.string.dont_have_an_account_yet))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${stringResource(id = R.string.sign_up)}!",
                color = MaterialTheme.colors.primary
            )
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.BackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}