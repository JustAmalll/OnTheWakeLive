package auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.LoginUseCase
import auth.presentation.login.LoginEvent.OnPhoneNumberChanged
import auth.presentation.login.LoginViewModel.LoginAction.NavigateToCreateAccountScreen
import core.domain.model.asString
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

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _action = Channel<LoginAction>()
    val actions: Flow<LoginAction> = _action.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.OnCreateAccountClicked -> viewModelScope.launch {
                _action.send(NavigateToCreateAccountScreen)
            }

            is LoginEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value) }
            is OnPhoneNumberChanged -> _state.update { it.copy(phoneNumber = event.value) }
            LoginEvent.OnSignInClicked -> signIn()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
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
            if (phoneNumberResult.isFailure || passwordResult.isFailure) {
                return@launch
            }
            _state.value = state.value.copy(isLoading = true)

            loginUseCase(
                phoneNumber = state.value.phoneNumber,
                password = state.value.password
            ).onSuccess {
                _action.send(LoginAction.NavigateToQueueScreen)
            }.onFailure { error ->
                _action.send(LoginAction.ShowError(errorMessage = error.asString()))
            }
            _state.value = state.value.copy(isLoading = false)
        }
    }

    sealed interface LoginAction {
        data object NavigateToQueueScreen : LoginAction
        data object NavigateToCreateAccountScreen : LoginAction
        data class ShowError(val errorMessage: String) : LoginAction
    }
}