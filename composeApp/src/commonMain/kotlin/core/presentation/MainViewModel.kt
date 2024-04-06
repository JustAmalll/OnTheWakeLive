package core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.AuthenticationUseCase
import auth.domain.use_case.GetUserIdUseCase
import auth.domain.use_case.IsUserAdminUseCase
import core.domain.utils.DataError
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import core.presentation.MainEvent.OnMainScreenAppeared
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val authenticationUseCase: AuthenticationUseCase,
    private val isUserAdminUseCase: IsUserAdminUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _action = Channel<MainAction>()
    val actions: Flow<MainAction> = _action.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            OnMainScreenAppeared -> {
                isUserAdmin()
                getUserId()
            }
        }
    }

    private fun isUserAdmin() {
        viewModelScope.launch {
            _state.update { it.copy(isUserAdmin = isUserAdminUseCase()) }
        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            _state.update { it.copy(userId = getUserIdUseCase()) }
            println("getUserId ${state.value.userId}")
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authenticationUseCase().onSuccess {
                _action.send(MainAction.NavigateToQueueScreen)
            }.onFailure { error ->
                if (error == DataError.Network.UNAUTHORIZED) {
                    _action.send(MainAction.NavigateToLoginScreen)
                } else {
                    _action.send(MainAction.NavigateToServerUnavailableScreen)
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed interface MainAction {
        data object NavigateToQueueScreen : MainAction
        data object NavigateToLoginScreen : MainAction
        data object NavigateToServerUnavailableScreen : MainAction
    }
}