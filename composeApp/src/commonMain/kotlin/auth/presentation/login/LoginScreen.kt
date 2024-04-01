package auth.presentation.login

import MainScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.LoginEvent.OnCreateAccountClicked
import auth.presentation.login.LoginEvent.OnPhoneNumberChanged
import auth.presentation.login.LoginViewModel.LoginAction.NavigateToCreateAccountScreen
import auth.presentation.login.LoginViewModel.LoginAction.NavigateToQueueScreen
import auth.presentation.login.LoginViewModel.LoginAction.ShowError
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardTextField
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.create
import onthewakelive.composeapp.generated.resources.dont_have_an_account_yet
import onthewakelive.composeapp.generated.resources.password
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class LoginAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToCreateAccountScreen -> navigator?.push(MainScreen)
                    NavigateToQueueScreen -> navigator?.push(MainScreen)
                    is ShowError -> {}
                }
            }
        }

        LoginScreen(state = state, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable { onEvent(OnCreateAccountClicked) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(resource = Res.string.dont_have_an_account_yet),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(resource = Res.string.create),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(all = 24.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(resource = Res.string.sign_in),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.phoneNumber,
                onValueChange = { onEvent(OnPhoneNumberChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.phone_number),
                error = state.phoneNumberError,
                isPhoneNumberTextField = true
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.password,
                onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
                label = stringResource(resource = Res.string.password),
                isPasswordTextField = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEvent(LoginEvent.OnSignInClicked)
                        focusManager.clearFocus()
                    }
                ),
                error = state.passwordError
            )
            Button(
                onClick = {
                    onEvent(LoginEvent.OnSignInClicked)
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(resource = Res.string.sign_in))
            }
        }
    }
}