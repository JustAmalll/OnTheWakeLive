package user_profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import auth.domain.use_case.LogoutUseCase
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
import user_profile.domain.use_case.GetUserProfileUseCase
import user_profile.presentation.UserProfileEvent.OnLogoutClicked

class UserProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _action = Channel<UserProfileAction>()
    val actions: Flow<UserProfileAction> = _action.receiveAsFlow()

    init {
        getUserProfile()
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            OnLogoutClicked -> logout()
            UserProfileEvent.OnEditProfileClicked -> TODO()
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().onSuccess { userProfile ->
                _state.update { it.copy(userProfile = userProfile) }
            }.onFailure { error ->
                _action.send(UserProfileAction.ShowError(error.asString()))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _action.send(UserProfileAction.NavigateToLoginScreen)
        }
    }

    sealed interface UserProfileAction {
        data object NavigateToLoginScreen : UserProfileAction
        data class ShowError(val message: String) : UserProfileAction
    }
}