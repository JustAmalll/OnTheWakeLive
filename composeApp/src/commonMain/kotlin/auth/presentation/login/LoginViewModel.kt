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
    private val login: LoginUseCase,
    private val validatePhoneNumber: ValidatePhoneNumberUseCase,
    private val validatePassword: ValidatePasswordUseCase
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
            val phoneNumberError = validatePhoneNumber(state.value.phoneNumber).errorOrNull()
            val passwordError = validatePassword(state.value.password).errorOrNull()

            _state.update {
                it.copy(
                    phoneNumberError = phoneNumberError?.asString(),
                    passwordError = passwordError?.asString()
                )
            }
            if (phoneNumberError != null || passwordError != null) {
                return@launch
            }
            _state.update { it.copy(isLoading = true) }

            login(
                phoneNumber = state.value.phoneNumber,
                password = state.value.password
            ).onSuccess {
                _action.send(LoginAction.NavigateToQueueScreen)
            }.onFailure { error ->
                _action.send(LoginAction.ShowError(errorMessage = error.asString()))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed interface LoginAction {
        data object NavigateToQueueScreen : LoginAction
        data object NavigateToCreateAccountScreen : LoginAction
        data class ShowError(val errorMessage: String) : LoginAction
    }
}