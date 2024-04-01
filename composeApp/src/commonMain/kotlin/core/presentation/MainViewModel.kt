package core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.AuthenticationUseCase
import core.domain.utils.NetworkError
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    private val _action = Channel<MainAction>()
    val actions: Flow<MainAction> = _action.receiveAsFlow()

    init {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            isLoading = true

            authenticationUseCase().onSuccess {
                _action.send(MainAction.NavigateToQueueScreen)
            }.onFailure { error ->
                if (error == NetworkError.UNAUTHORIZED) {
                    _action.send(MainAction.NavigateToLoginScreen)
                } else {
                    _action.send(MainAction.NavigateToServerUnavailableScreen)
                }
            }
            isLoading = false
        }
    }

    sealed interface MainAction {
        data object NavigateToQueueScreen : MainAction
        data object NavigateToLoginScreen : MainAction
        data object NavigateToServerUnavailableScreen : MainAction
    }
}