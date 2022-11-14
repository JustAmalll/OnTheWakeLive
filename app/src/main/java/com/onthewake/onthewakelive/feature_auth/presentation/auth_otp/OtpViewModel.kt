package com.onthewake.onthewakelive.feature_auth.presentation.auth_otp

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.feature_auth.presentation.AuthState
import com.onthewake.onthewakelive.feature_auth.presentation.RegisterData
import com.onthewake.onthewakelive.util.Constants
import com.onthewake.onthewakelive.util.fromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val context: Application,
    private val validationUseCase: ValidationUseCase,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(context) {

    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    fun onEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.OtpCodeChanged -> {
                state = state.copy(otp = event.value)
            }
            is OtpUiEvent.VerifyOtpAndSignUp -> {
                savedStateHandle.get<String>(
                    Constants.REGISTER_DATA_ARGUMENT_KEY
                )?.let { registerData ->
                    verifyOtpAndSignUp(registerData.fromJson(RegisterData::class.java))
                }
            }
        }
    }

    private fun verifyOtpAndSignUp(registerData: RegisterData) {

        val otpValidationResult = validationUseCase.validateOtp(state.otp)
        if (!otpValidationResult.successful) {
            state = state.copy(otpError = otpValidationResult.errorMessage)
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val otpResult = repository.verifyOtp(state.otp)

            if (otpResult is AuthResult.OtpVerified) {
                val signUpResult = repository.signUp(
                    firstName = registerData.firstName,
                    lastName = registerData.lastName,
                    phoneNumber = registerData.phoneNumber,
                    password = registerData.password
                )
                context.dataStore.updateData { profile ->
                    profile.copy(
                        firstName = registerData.firstName,
                        lastName = registerData.lastName,
                        phoneNumber = registerData.phoneNumber
                    )
                }
                resultChannel.send(signUpResult)
            } else {
                resultChannel.send(AuthResult.IncorrectOtp())
            }
            state = state.copy(isLoading = false)
        }
    }
}