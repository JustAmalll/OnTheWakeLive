package core.presentation

import MainScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.AuthenticationUseCase
import auth.domain.use_case.GetUserIdUseCase
import auth.domain.use_case.IsUserAdminUseCase
import auth.presentation.login.LoginAssembly
import com.mmk.kmpnotifier.notification.NotifierManager
import core.domain.utils.DataError
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import core.presentation.MainEvent.OnMainScreenAppeared
import core.presentation.MainEvent.ToggleNavigationBarVisibility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paywall.domain.repository.PaywallRepository
import paywall.presentation.in_processing.PaywallInProcessingAssembly
import queue.domain.use_case.UpdateNotificationTokenUseCase
import server_unavailable.ServerUnavailableAssembly

class MainViewModel(
    private val paywallRepository: PaywallRepository,
    private val authenticationUseCase: AuthenticationUseCase,
    private val isUserAdminUseCase: IsUserAdminUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateNotificationTokenUseCase: UpdateNotificationTokenUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        authenticate()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            OnMainScreenAppeared -> {
                isUserAdmin()
                getUserId()
            }

            ToggleNavigationBarVisibility -> _state.update {
                it.copy(isNavigationBarVisible = !it.isNavigationBarVisible)
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
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authenticationUseCase().onSuccess {
                _state.update {
                    it.copy(
                        startScreen = if (paywallRepository.isPaymentInProcessing()) {
                            PaywallInProcessingAssembly()
                        } else {
                            MainScreen
                        }
                    )
                }
                listenForNotificationTokenChange()
            }.onFailure { error ->
                if (error == DataError.Network.UNAUTHORIZED) {
                    _state.update { it.copy(startScreen = LoginAssembly()) }
                } else {
                    _state.update { it.copy(startScreen = ServerUnavailableAssembly()) }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun listenForNotificationTokenChange() {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                viewModelScope.launch {
                    updateNotificationTokenUseCase(newToken = token)
                }
            }
        })
    }
}