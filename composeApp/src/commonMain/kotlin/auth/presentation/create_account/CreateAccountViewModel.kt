package auth.presentation.create_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.CreateAccountUseCase
import core.domain.use_case.validation.ValidateFirstNameUseCase
import core.domain.use_case.validation.ValidateLastNameUseCase
import core.domain.use_case.validation.ValidatePasswordUseCase
import core.domain.use_case.validation.ValidatePhoneNumberUseCase
import auth.presentation.create_account.CreateAccountEvent.OnCreateAccountClicked
import auth.presentation.create_account.CreateAccountEvent.OnFirstNameChanged
import auth.presentation.create_account.CreateAccountEvent.OnLastNameChanged
import auth.presentation.create_account.CreateAccountEvent.OnLoginClicked
import auth.presentation.create_account.CreateAccountEvent.OnPasswordChanged
import auth.presentation.create_account.CreateAccountEvent.OnPhoneNumberChanged
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToLoginScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.NavigateToQueueScreen
import auth.presentation.create_account.CreateAccountViewModel.CreateAccountAction.ShowError
import core.domain.model.asString
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

class CreateAccountViewModel(
    private val createAccountUseCase: CreateAccountUseCase,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateLastNameUseCase: ValidateLastNameUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
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
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            val firstNameResult = validateFirstNameUseCase(
                firstName = state.value.firstName
            ).onFailure { error ->
                _state.update { it.copy(firstNameError = error.asString()) }
            }
            val lastNameError = validateLastNameUseCase(
                lastName = state.value.lastName
            ).onFailure { error ->
                _state.update { it.copy(lastNameError = error.asString()) }
            }
            val phoneNumberResult = validatePhoneNumberUseCase(
                phoneNumber = state.value.phoneNumber
            ).onFailure { error ->
                _state.update { it.copy(phoneNumberError = error.asString()) }
            }
            val passwordResult = validatePasswordUseCase(
                password = state.value.password
            ).onFailure { error ->
                _state.update { it.copy(passwordError = error.asString()) }
            }
            val hasError = listOf(
                firstNameResult.isFailure,
                lastNameError.isFailure,
                phoneNumberResult.isFailure,
                passwordResult.isFailure
            ).any { it }

            if (hasError) {
                return@launch
            }
            _state.value = state.value.copy(isLoading = true)

            createAccountUseCase(
                firstName = state.value.firstName,
                lastName = state.value.lastName,
                phoneNumber = state.value.phoneNumber,
                password = state.value.password
            ).onSuccess {
                _action.send(NavigateToQueueScreen)
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }
            _state.value = state.value.copy(isLoading = false)
        }
    }

    sealed interface CreateAccountAction {
        data object NavigateToQueueScreen : CreateAccountAction
        data object NavigateToLoginScreen : CreateAccountAction
        data class ShowError(val errorMessage: String) : CreateAccountAction
    }
}