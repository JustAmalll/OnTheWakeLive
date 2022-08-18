package com.onthewake.onthewakelive.feature_auth.presentation

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.util.Constants
import kotlinx.coroutines.channels.Channel
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
            is AuthUiEvent.SignUpTelegramChanged -> {
                state = state.copy(signUpTelegram = event.value)
            }
            is AuthUiEvent.SignUpInstagramChanged -> {
                state = state.copy(signUpInstagram = event.value)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.value)
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp() {
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
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signUp(
                firstName = state.signUpFirsName,
                lastName = state.signUpLastName,
                phoneNumber = state.signUpPhoneNumber,
                telegram = state.signUpTelegram,
                instagram = state.signUpInstagram,
                password = state.signUpPassword
            )
            resultChannel.send(result)
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
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(
                phoneNumber = state.signInPhoneNumber,
                password = state.signInPassword
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}