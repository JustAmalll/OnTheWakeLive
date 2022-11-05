package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.feature_profile.domain.module.UpdateProfileData
import com.onthewake.onthewakelive.feature_profile.domain.repository.ProfileRepository
import com.onthewake.onthewakelive.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val validationUseCase: ValidationUseCase,
    private val profileRepository: ProfileRepository,
    private val context: Application
) : AndroidViewModel(context) {

    var state by mutableStateOf(ProfileStateErrors())

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val phoneNumber = mutableStateOf("")
    val telegram = mutableStateOf("")
    val userProfilePictureUri = mutableStateOf("")
    val instagram = mutableStateOf("")
    val dateOfBirth = mutableStateOf("")

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val _navigateUp = MutableSharedFlow<Boolean>()
    val navigateUp = _navigateUp.asSharedFlow()

    private val _profilePictureUri = mutableStateOf<Uri?>(null)
    val profilePictureUri: State<Uri?> = _profilePictureUri

    fun onEvent(event: EditProfileUiEvent) {
        when (event) {
            is EditProfileUiEvent.EditProfileFirstNameChanged -> {
                firstName.value = event.value
            }
            is EditProfileUiEvent.EditProfileLastNameChanged -> {
                lastName.value = event.value
            }
            is EditProfileUiEvent.EditProfilePhoneNumberChanged -> {
                phoneNumber.value = event.value
            }
            is EditProfileUiEvent.EditProfileTelegramChanged -> {
                telegram.value = event.value
            }
            is EditProfileUiEvent.EditProfileInstagramChanged -> {
                instagram.value = event.value
            }
            is EditProfileUiEvent.EditProfileDateOfBirthChanged -> {
                dateOfBirth.value = event.value
            }
            is EditProfileUiEvent.CropImage -> {
                _profilePictureUri.value = event.uri
            }
            is EditProfileUiEvent.EditProfile -> {
                editProfile()
            }
        }
    }

    private fun editProfile() {
        val firstNameResult = validationUseCase.validateFirstName(firstName.value)
        val lastNameResult = validationUseCase.validateLastName(lastName.value)
        val phoneNumberResult = validationUseCase.validatePhoneNumber(phoneNumber.value)
        val dateOfBirthResult = validationUseCase.validateDateOfBirth(dateOfBirth.value)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            phoneNumberResult,
            dateOfBirthResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                profileFirsNameError = firstNameResult.errorMessage,
                profileLastNameError = lastNameResult.errorMessage,
                profilePhoneNumberError = phoneNumberResult.errorMessage,
                profileDateOfBirthError = dateOfBirthResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val profilePictureFileName = profilePictureUri.value?.toFile()?.name ?: userProfilePictureUri.value.toUri().toFile().name
            val profilePictureUri = profilePictureUri.value ?: userProfilePictureUri.value

            println(profilePictureFileName)

            val result = profileRepository.updateProfile(
                updateProfileData = UpdateProfileData(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    phoneNumber = phoneNumber.value,
                    instagram = instagram.value,
                    telegram = telegram.value,
                    dateOfBirth = dateOfBirth.value,
                    profilePictureFileName = profilePictureFileName
                ),
                profilePictureUri = profilePictureUri.toString()
            )
            context.dataStore.updateData {
                it.copy(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    phoneNumber = phoneNumber.value,
                    instagram = instagram.value,
                    telegram = telegram.value,
                    dateOfBirth = dateOfBirth.value,
                    profilePictureUri = profilePictureUri.toString()
                )
            }
            state = state.copy(isLoading = false)
            when (result) {
                is Resource.Success -> {
                    _snackBarEvent.emit("Successfully updated profile")
                }
                is Resource.Error -> {
                    _snackBarEvent.emit("Unknown error")
                }
            }
        }
    }
}