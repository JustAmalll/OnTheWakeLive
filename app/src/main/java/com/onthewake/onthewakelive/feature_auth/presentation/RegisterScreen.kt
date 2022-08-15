package com.onthewake.onthewakelive.feature_auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.DefaultTextField
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.ui.theme.BackgroundColor

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    navController.navigate(Screen.QueueScreen.route) {
                        popUpTo(Screen.RegisterScreen.route) { inclusive = true }
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
                is AuthResult.UserAlreadyExist -> {
                    showSnackBar(context.getString(R.string.user_already_exist))
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
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signUpFirsName,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpFirstNameChanged(it))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(id = R.string.first_name),
                isError = state.signUpFirsNameError != null,
                errorText = state.signUpFirsNameError
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signUpLastName,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpLastNameChanged(it))
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
            DefaultTextField(
                value = state.signUpPhoneNumber,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpPhoneNumberChanged(it))
                },
                label = stringResource(id = R.string.phone_number),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = state.signUpPhoneNumberError != null,
                errorText = state.signUpPhoneNumberError
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signUpTelegram,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpTelegramChanged(it))
                },
                label = stringResource(id = R.string.telegram)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signUpInstagram,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpInstagramChanged(it))
                },
                label = stringResource(id = R.string.instagram)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultTextField(
                value = state.signUpPassword,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it))
                },
                label = stringResource(id = R.string.password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                isPasswordTextField = true,
                isError = state.signUpPasswordError != null,
                errorText = state.signUpPasswordError
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(AuthUiEvent.SignUp) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(id = R.string.sign_up), color = Color.White)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable {
                    navController.navigate(Screen.LoginScreen.route)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.already_have_an_account))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.sign_in),
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