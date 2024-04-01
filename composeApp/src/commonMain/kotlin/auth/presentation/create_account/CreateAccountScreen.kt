package auth.presentation.create_account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToLoginScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToQueueScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.ShowError
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardTextField
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.already_have_an_account
import onthewakelive.composeapp.generated.resources.create_account
import onthewakelive.composeapp.generated.resources.first_name
import onthewakelive.composeapp.generated.resources.last_name
import onthewakelive.composeapp.generated.resources.password
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.sign_in
import onthewakelive.composeapp.generated.resources.sign_up
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import queue.presentation.list.QueueAssembly

class CreateAccountAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateAccountViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToLoginScreen -> navigator?.push(LoginAssembly())
                    NavigateToQueueScreen -> navigator?.push(QueueAssembly)
                    is ShowError -> TODO()
                }
            }
        }
        CreateAccountScreen(state = state, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreateAccountScreen(
    state: CreateAccountState,
    onEvent: (CreateAccountEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable { onEvent(CreateAccountEvent.OnLoginClicked) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(resource = Res.string.already_have_an_account),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(resource = Res.string.sign_in) + "!",
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
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(resource = Res.string.sign_up),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.firstName,
                onValueChange = { onEvent(CreateAccountEvent.OnFirstNameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.first_name),
                error = state.firstNameError
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.lastName,
                onValueChange = { onEvent(CreateAccountEvent.OnLastNameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.last_name),
                error = state.lastNameError
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.phoneNumber,
                onValueChange = { onEvent(CreateAccountEvent.OnPhoneNumberChanged(it)) },
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
                onValueChange = { onEvent(CreateAccountEvent.OnPasswordChanged(it)) },
                label = stringResource(resource = Res.string.password),
                isPasswordTextField = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEvent(CreateAccountEvent.OnCreateAccountClicked)
                        focusManager.clearFocus()
                    }
                ),
                error = state.passwordError
            )
            Button(
                onClick = {
                    onEvent(CreateAccountEvent.OnCreateAccountClicked)
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(resource = Res.string.create_account))
            }
        }
    }
}