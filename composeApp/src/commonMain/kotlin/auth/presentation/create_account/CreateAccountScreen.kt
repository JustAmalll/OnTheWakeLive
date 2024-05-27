package auth.presentation.create_account

import MainScreen
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.create_account.CreateAccountEvent.OnLoginClicked
import auth.presentation.create_account.CreateAccountEvent.OnTogglePasswordVisibilityClicked
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToLoginScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToQueueScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.ShowError
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardButton
import core.presentation.components.StandardTextField
import core.presentation.utils.clickableWithoutIndication
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

class CreateAccountAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateAccountViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToLoginScreen -> navigator?.push(LoginAssembly())
                    NavigateToQueueScreen -> navigator?.replaceAll(MainScreen)
                    is ShowError -> snackBarHostState.showSnackbar(action.errorMessage)
                }
            }
        }
        CreateAccountScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    state: CreateAccountState,
    snackBarHostState: SnackbarHostState,
    onEvent: (CreateAccountEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(resource = Res.string.sign_up),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            StandardTextField(
                value = state.firstName,
                onValueChange = { onEvent(CreateAccountEvent.OnFirstNameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.first_name),
                supportingText = state.firstNameError
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
                supportingText = state.lastNameError
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
                supportingText = state.phoneNumberError,
                isPhoneNumberTextField = true
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.password,
                onValueChange = { onEvent(CreateAccountEvent.OnPasswordChanged(it)) },
                label = stringResource(resource = Res.string.password),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { onEvent(OnTogglePasswordVisibilityClicked) }) {
                        Icon(
                            imageVector = if (state.showPassword) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = null
                        )
                    }
                },
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
                supportingText = state.passwordError
            )
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                onClick = {
                    onEvent(CreateAccountEvent.OnCreateAccountClicked)
                    focusManager.clearFocus()
                },
                text = stringResource(resource = Res.string.create_account),
                isLoading = state.isLoading
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 26.dp)
                    .clickableWithoutIndication { onEvent(OnLoginClicked) },
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
    }
}