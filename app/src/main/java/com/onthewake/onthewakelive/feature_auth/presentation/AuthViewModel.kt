package com.onthewake.onthewakelive.feature_auth.presentation

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.feature_auth.data.remote.request.AuthRequest
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.util.findActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    private val _navigateUpEvent = MutableSharedFlow<RegisterData>()
    val navigateUpEvent = _navigateUpEvent.asSharedFlow()

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.SignInPhoneNumberChanged -> {
                state = state.copy(signInPhoneNumber = event.value)
            }
            is AuthUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.SignUpFirstNameChanged -> {
                state = state.copy(signUpFirsName = event.value)
            }
            is AuthUiEvent.SignUpLastNameChanged -> {
                state = state.copy(signUpLastName = event.value)
            }
            is AuthUiEvent.SignUpPhoneNumberChanged -> {
                state = state.copy(signUpPhoneNumber = event.value)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.value)
            }
            is AuthUiEvent.SendOtp -> {
                sendOtp(event.context.findActivity())
            }
        }
    }

    private fun sendOtp(activity: Activity) {
        val firstNameResult = validationUseCase.validateFirstName(state.signUpFirsName)
        val lastNameResult = validationUseCase.validateLastName(state.signUpLastName)
        val phoneNumberResult = validationUseCase.validatePhoneNumber(state.signUpPhoneNumber)
        val passwordResult = validationUseCase.validatePassword(state.signUpPassword)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            phoneNumberResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                signUpFirsNameError = firstNameResult.errorMessage,
                signUpLastNameError = lastNameResult.errorMessage,
                signUpPhoneNumberError = phoneNumberResult.errorMessage,
                signUpPasswordError = passwordResult.errorMessage
            )
            return
        } else {
            state = state.copy(
                signUpFirsNameError = null,
                signUpLastNameError = null,
                signUpPhoneNumberError = null,
                signUpPasswordError = null
            )
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            if (!repository.checkIfUserAlreadyExists(state.signUpPhoneNumber)) {
                _navigateUpEvent.emit(
                    RegisterData(
                        firstName = state.signUpFirsName.trim(),
                        lastName = state.signUpLastName.trim(),
                        phoneNumber = state.signUpPhoneNumber.trim(),
                        password = state.signUpPassword.trim()
                    )
                )
                val result = repository.sendOtp(state.signUpPhoneNumber, activity)
                resultChannel.send(result)
            } else {
                resultChannel.send(AuthResult.UserAlreadyExist())
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun signIn() {
        val phoneNumberResult = validationUseCase.validatePhoneNumber(state.signInPhoneNumber)
        val passwordResult = validationUseCase.validatePassword(state.signInPassword)

        val hasError = listOf(
            phoneNumberResult, passwordResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                signInPhoneNumberError = phoneNumberResult.errorMessage,
                signInPasswordError = passwordResult.errorMessage
            )
            return
        } else {
            state = state.copy(
                signInPhoneNumberError = null,
                signInPasswordError = null
            )
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(
                AuthRequest(
                    phoneNumber = state.signInPhoneNumber.trim(),
                    password = state.signInPassword.trim()
                )
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}