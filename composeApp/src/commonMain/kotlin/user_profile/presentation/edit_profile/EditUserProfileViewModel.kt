package user_profile.presentation.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import user_profile.domain.model.UserProfile
import user_profile.domain.use_case.EditProfileUseCase
import user_profile.domain.use_case.GetUserProfileUseCase
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnEditProfileClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnFirstNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnInstagramChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnLastNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnNavigateBackClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnTelegramChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnUserPhotoSelected
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.NavigateBack
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.ShowError
import user_profile.presentation.edit_profile.mapper.toEditUserProfileState

class EditUserProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditUserProfileState())
    val state: StateFlow<EditUserProfileState> = _state.asStateFlow()

    private val _action = Channel<EditUserProfileAction>()
    val actions: Flow<EditUserProfileAction> = _action.receiveAsFlow()

    private var userId: String? = null

    init {
        getUserProfile()
    }

    fun onEvent(event: EditUserProfileEvent) {
        when (event) {
            is OnFirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is OnLastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is OnInstagramChanged -> _state.update { it.copy(instagram = event.value) }
            is OnTelegramChanged -> _state.update { it.copy(telegram = event.value) }
            is OnUserPhotoSelected -> _state.update { it.copy(photo = event.byteArray) }

            OnNavigateBackClicked -> viewModelScope.launch {
                _action.send(NavigateBack)
            }

            OnEditProfileClicked -> editUserProfile()
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().onSuccess { userProfile ->
                userId = userProfile.userId
                _state.update { userProfile.toEditUserProfileState(state = it) }
            }.onFailure { error ->
                _action.send(ShowError(error.asString()))
            }
        }
    }

    private fun editUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            editProfileUseCase(
                userProfile = UserProfile(
                    userId = userId ?: return@launch,
                    firstName = state.value.firstName,
                    lastName = state.value.lastName,
                    phoneNumber = state.value.phoneNumber,
                    instagram = state.value.instagram,
                    telegram = state.value.telegram,
                    photo = state.value.photo
                )
            ).onSuccess {

            }.onFailure { error ->
                _action.send(ShowError(error.toString()))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed interface EditUserProfileAction {
        data object NavigateBack : EditUserProfileAction
        data class ShowError(val message: String) : EditUserProfileAction
    }
}