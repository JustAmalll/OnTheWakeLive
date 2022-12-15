package com.onthewake.onthewakelive.feature_auth.presentation.auth_register

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val resultChannel = Channel<AuthResult<Unit>>()
    val registerResults = resultChannel.receiveAsFlow()

    private val _navigateUpEvent = MutableSharedFlow<RegisterData>()
    val navigateUpEvent = _navigateUpEvent.asSharedFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.SignUpFirstNameChanged -> {
                state = state.copy(signUpFirstName = event.value)
            }
            is RegisterUiEvent.SignUpLastNameChanged -> {
                state = state.copy(signUpLastName = event.value)
            }
            is RegisterUiEvent.SignUpPhoneNumberChanged -> {
                state = state.copy(signUpPhoneNumber = event.value)
            }
            is RegisterUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.value)
            }
            is RegisterUiEvent.SendOtp -> {
                sendOtp(event.context.findActivity())
            }
        }
    }

    private fun sendOtp(activity: Activity) {
        val firstNameResult = validationUseCase.validateFirstName(state.signUpFirstName)
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
                signUpFirstNameError = firstNameResult.errorMessage,
                signUpLastNameError = lastNameResult.errorMessage,
                signUpPhoneNumberError = phoneNumberResult.errorMessage,
                signUpPasswordError = passwordResult.errorMessage
            )
            return
        } else {
            state = state.copy(
                signUpFirstNameError = null,
                signUpLastNameError = null,
                signUpPhoneNumberError = null,
                signUpPasswordError = null
            )
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val isUserAlreadyExists = repository.isUserAlreadyExists(state.signUpPhoneNumber)

            if (!isUserAlreadyExists) {
                val result = repository.sendOtp(
                    phoneNumber = state.signUpPhoneNumber.trim(),
                    activity = activity,
                    isResendAction = false
                )
                if (result is AuthResult.OtpSentSuccess)
                    _navigateUpEvent.emit(
                        RegisterData(
                            firstName = state.signUpFirstName.trim(),
                            lastName = state.signUpLastName.trim(),
                            phoneNumber = state.signUpPhoneNumber.trim(),
                            password = state.signUpPassword.trim()
                        )
                    )
                resultChannel.send(result)
            } else {
                resultChannel.send(AuthResult.UserAlreadyExist())
            }
            state = state.copy(isLoading = false)
        }
    }
}