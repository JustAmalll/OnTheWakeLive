package com.onthewake.onthewakelive.feature_auth.presentation.auth_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.feature_auth.data.remote.request.AuthRequest
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val resultChannel = Channel<AuthResult<Unit>>()
    val loginResults = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.SignInPhoneNumberChanged -> state = state.copy(
                signInPhoneNumber = event.value
            )
            is LoginUiEvent.SignInPasswordChanged -> state = state.copy(
                signInPassword = event.value
            )
            is LoginUiEvent.SignIn -> signIn()
        }
    }

    private fun signIn() {
        val phoneNumberResult = validationUseCase.validatePhoneNumber(state.signInPhoneNumber)
        val passwordResult = validationUseCase.validatePassword(state.signInPassword)

        val hasError = listOf(phoneNumberResult, passwordResult).any { !it.successful }

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