package com.onthewake.onthewakelive.feature_profile.presentation.profile

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.core.presentation.dataStore
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_profile.domain.repository.ProfileRepository
import com.onthewake.onthewakelive.core.util.Constants
import com.onthewake.onthewakelive.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val context: Application,
    private val prefs: SharedPreferences
) : AndroidViewModel(context) {

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    val isLoading = mutableStateOf(false)

    fun getProfile() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = profileRepository.getProfile()) {
                is Resource.Success -> {
                    result.data?.let {
                        context.dataStore.updateData { profile ->
                            profile.copy(
                                firstName = it.firstName,
                                lastName = it.lastName,
                                phoneNumber = it.phoneNumber,
                                instagram = it.instagram,
                                telegram = it.telegram,
                                dateOfBirth = it.dateOfBirth,
                                profilePictureUri = it.profilePictureUri
                            )
                        }
                    }
                    isLoading.value = false
                }
                is Resource.Error -> {
                    isLoading.value = false
                    _snackBarEvent.emit(result.message ?: "Unknown Error")
                }
            }
        }
    }

    fun logout() {
        prefs.edit().putString(Constants.PREFS_JWT_TOKEN, null).apply()
        viewModelScope.launch {
            context.dataStore.updateData {
                Profile(
                    userId = "",
                    firstName = "",
                    lastName = "",
                    phoneNumber = "",
                    instagram = "",
                    telegram = "",
                    dateOfBirth = "",
                    profilePictureUri = ""
                )
            }
        }
    }
}