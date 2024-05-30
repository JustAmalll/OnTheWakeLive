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
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.successfully_updated_profile
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import queue.domain.use_case.CloseSessionUseCase
import user_profile.domain.model.UpdateUserProfileRequest
import user_profile.domain.use_case.EditProfileUseCase
import user_profile.domain.use_case.GetUserProfileUseCase
import user_profile.presentation.UserProfileEvent.OnDeleteUserPhotoClicked
import user_profile.presentation.UserProfileEvent.OnEditProfileClicked
import user_profile.presentation.UserProfileEvent.OnFirstNameChanged
import user_profile.presentation.UserProfileEvent.OnInstagramChanged
import user_profile.presentation.UserProfileEvent.OnLastNameChanged
import user_profile.presentation.UserProfileEvent.OnLogoutClicked
import user_profile.presentation.UserProfileEvent.OnTelegramChanged
import user_profile.presentation.UserProfileEvent.OnUserPhotoClicked
import user_profile.presentation.UserProfileEvent.OnUserPhotoCropped
import user_profile.presentation.UserProfileEvent.OnUserPhotoSelected
import user_profile.presentation.UserProfileViewModel.UserProfileAction.NavigateToFullSizePhotoScreen
import user_profile.presentation.UserProfileViewModel.UserProfileAction.ShowError

class UserProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _action = Channel<UserProfileAction>()
    val actions: Flow<UserProfileAction> = _action.receiveAsFlow()

    private var userId: Int? = null

    init {
        getUserProfile()
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is OnFirstNameChanged -> _state.update {
                it.copy(firstName = event.value, hasChanges = true)
            }

            is OnLastNameChanged -> _state.update {
                it.copy(lastName = event.value, hasChanges = true)
            }

            is OnInstagramChanged -> _state.update {
                it.copy(instagram = event.value, hasChanges = true)
            }

            is OnTelegramChanged -> _state.update {
                it.copy(telegram = event.value, hasChanges = true)
            }

            OnLogoutClicked -> logout()
            OnEditProfileClicked -> editUserProfile()

            is OnUserPhotoClicked -> viewModelScope.launch {
                state.value.photo?.let { photo ->
                    _action.send(NavigateToFullSizePhotoScreen(photo = photo))
                }
            }

            OnDeleteUserPhotoClicked -> _state.update {
                it.copy(photo = null, newPhotoBytes = null, hasChanges = true)
            }

            is OnUserPhotoCropped -> _state.update {
                it.copy(
                    newPhotoBytes = event.byteArray,
                    showImageCropper = false,
                    photo = null,
                    hasChanges = true
                )
            }

            is OnUserPhotoSelected -> _state.update {
                it.copy(newPhotoBytes = event.byteArray, showImageCropper = true)
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun editUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            editProfileUseCase(
                updateRequest = UpdateUserProfileRequest(
                    userId = userId ?: return@launch,
                    firstName = state.value.firstName,
                    lastName = state.value.lastName,
                    instagram = state.value.instagram,
                    telegram = state.value.telegram,
                    photo = state.value.photo
                ),
                newPhotoBytes = state.value.newPhotoBytes
            ).onSuccess {
                _state.update { it.copy(hasChanges = false) }
                _action.send(ShowError(getString(Res.string.successfully_updated_profile)))
            }.onFailure { error ->
                _action.send(ShowError(error.asString()))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().onSuccess { userProfile ->
                userId = userProfile.userId

                _state.update {
                    it.copy(
                        firstName = userProfile.firstName,
                        lastName = userProfile.lastName,
                        phoneNumber = userProfile.phoneNumber,
                        instagram = userProfile.instagram ?: "",
                        telegram = userProfile.telegram ?: "",
                        photo = userProfile.photo
                    )
                }
            }.onFailure { error ->
                _action.send(ShowError(error.asString()))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            closeSessionUseCase()
            _action.send(UserProfileAction.NavigateToLoginScreen)
        }
    }

    sealed interface UserProfileAction {
        data class ShowError(val message: String) : UserProfileAction
        data object NavigateToLoginScreen : UserProfileAction
        data class NavigateToFullSizePhotoScreen(val photo: String) : UserProfileAction
    }
}