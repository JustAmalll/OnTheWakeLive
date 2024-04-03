package user_profile.presentation.profile

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
import user_profile.presentation.profile.UserProfileEvent.OnEditProfileClicked
import user_profile.presentation.profile.UserProfileEvent.OnLogoutClicked
import user_profile.presentation.profile.UserProfileEvent.OnUserPhotoClicked
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.NavigateToEditProfileScreen
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.NavigateToFullSizePhotoScreen

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

            OnEditProfileClicked -> viewModelScope.launch {
                _action.send(NavigateToEditProfileScreen)
            }

            is OnUserPhotoClicked -> viewModelScope.launch {
                state.value.userProfile?.photo?.let { photo ->
                    _action.send(NavigateToFullSizePhotoScreen(photo = photo))
                }
            }
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
        data class ShowError(val message: String) : UserProfileAction
        data object NavigateToLoginScreen : UserProfileAction
        data object NavigateToEditProfileScreen : UserProfileAction

        @Suppress("ArrayInDataClass")
        data class NavigateToFullSizePhotoScreen(val photo: ByteArray) : UserProfileAction
    }
}