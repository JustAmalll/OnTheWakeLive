package auth.presentation.create_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.CreateAccountUseCase
import auth.domain.use_case.IsUserAlreadyExistsUseCase
import auth.presentation.create_account.CreateAccountEvent.OnCreateAccountClicked
import auth.presentation.create_account.CreateAccountEvent.OnFirstNameChanged
import auth.presentation.create_account.CreateAccountEvent.OnLastNameChanged
import auth.presentation.create_account.CreateAccountEvent.OnLoginClicked
import auth.presentation.create_account.CreateAccountEvent.OnPasswordChanged
import auth.presentation.create_account.CreateAccountEvent.OnPhoneNumberChanged
import auth.presentation.create_account.CreateAccountEvent.OnTogglePasswordVisibilityClicked
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToLoginScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToQueueScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.ShowError
import core.domain.model.asString
import core.domain.use_case.validation.ValidateFirstNameUseCase
import core.domain.use_case.validation.ValidateLastNameUseCase
import core.domain.use_case.validation.ValidatePasswordUseCase
import core.domain.use_case.validation.ValidatePhoneNumberUseCase
import core.domain.utils.asString
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.user_already_exists
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

class CreateAccountViewModel(
    private val createAccount: CreateAccountUseCase,
    private val validateFirstName: ValidateFirstNameUseCase,
    private val validateLastName: ValidateLastNameUseCase,
    private val validatePhoneNumber: ValidatePhoneNumberUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val isUserAlreadyExists: IsUserAlreadyExistsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState> = _state.asStateFlow()

    private val _action = Channel<CreateAccountAction>()
    val actions: Flow<CreateAccountAction> = _action.receiveAsFlow()

    fun onEvent(event: CreateAccountEvent) {
        when (event) {
            is OnFirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is OnLastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is OnPasswordChanged -> _state.update { it.copy(password = event.value) }
            is OnPhoneNumberChanged -> _state.update { it.copy(phoneNumber = event.value) }
            OnCreateAccountClicked -> createAccount()
            OnLoginClicked -> viewModelScope.launch {
                _action.send(NavigateToLoginScreen)
            }

            OnTogglePasswordVisibilityClicked -> _state.update {
                it.copy(showPassword = !it.showPassword)
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun createAccount() {
        viewModelScope.launch {
            val firstNameError = validateFirstName(state.value.firstName).errorOrNull()
            val lastNameError = validateLastName(state.value.lastName).errorOrNull()
            val phoneNumberError = validatePhoneNumber(state.value.phoneNumber).errorOrNull()
            val passwordError = validatePassword(state.value.password).errorOrNull()

            _state.update {
                it.copy(
                    firstNameError = firstNameError?.asString(),
                    lastNameError = lastNameError?.asString(),
                    phoneNumberError = phoneNumberError?.asString(),
                    passwordError = passwordError?.asString()
                )
            }
            val hasError = listOf(
                firstNameError != null,
                lastNameError != null,
                phoneNumberError != null,
                passwordError != null
            ).any { it }

            if (hasError) {
                return@launch
            }
            _state.update { it.copy(isLoading = true) }

            isUserAlreadyExists(phoneNumber = state.value.phoneNumber).onSuccess { exists ->
                if (exists) {
                    _action.send(ShowError(getString(resource = Res.string.user_already_exists)))
                    _state.update { it.copy(isLoading = false) }
                    return@launch
                }

                createAccount(
                    firstName = state.value.firstName,
                    lastName = state.value.lastName,
                    phoneNumber = state.value.phoneNumber,
                    password = state.value.password
                ).onSuccess {
                    _action.send(NavigateToQueueScreen)
                }.onFailure { error ->
                    _action.send(ShowError(errorMessage = error.asString()))
                }
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed interface CreateAccountAction {
        data object NavigateToQueueScreen : CreateAccountAction
        data object NavigateToLoginScreen : CreateAccountAction
        data class ShowError(val errorMessage: String) : CreateAccountAction
    }
}