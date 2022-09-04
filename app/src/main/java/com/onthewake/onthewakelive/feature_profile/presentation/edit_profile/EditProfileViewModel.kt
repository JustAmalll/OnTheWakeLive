package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(EditProfileState())

    private val _chosenImageUri = mutableStateOf<Uri?>(null)
    val chosenImageUri: State<Uri?> = _chosenImageUri

    fun onEvent(event: EditProfileUiEvent) {
        when (event) {
            is EditProfileUiEvent.EditProfileFirstNameChanged -> {
                state = state.copy(profileFirsName = event.value)
            }
            is EditProfileUiEvent.EditProfileLastNameChanged -> {
                state = state.copy(profileLastName = event.value)
            }
            is EditProfileUiEvent.EditProfilePhoneNumberChanged -> {
                state = state.copy(profilePhoneNumber = event.value)
            }
            is EditProfileUiEvent.EditProfileTelegramChanged -> {
                state = state.copy(profileTelegram = event.value)
            }
            is EditProfileUiEvent.EditProfileInstagramChanged -> {
                state = state.copy(profileInstagram = event.value)
            }
            is EditProfileUiEvent.PickImage -> {
                _chosenImageUri.value = event.uri
            }
            is EditProfileUiEvent.CropImage -> {
                _chosenImageUri.value = event.uri
            }
            is EditProfileUiEvent.EditEditProfile -> {
                editProfile()
            }
        }
    }

    private fun editProfile() {

    }
}