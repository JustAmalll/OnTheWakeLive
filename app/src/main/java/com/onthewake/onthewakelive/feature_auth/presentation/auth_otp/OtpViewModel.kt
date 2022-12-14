package com.onthewake.onthewakelive.feature_auth.presentation.auth_otp

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.feature_auth.data.remote.request.CreateAccountRequest
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.feature_auth.presentation.auth_register.RegisterData
import com.onthewake.onthewakelive.feature_auth.presentation.auth_register.RegisterState
import com.onthewake.onthewakelive.util.Constants.REGISTER_DATA_ARGUMENT_KEY
import com.onthewake.onthewakelive.util.findActivity
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
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(context) {

    var state by mutableStateOf(RegisterState())
        private set

    private val resultChannel = Channel<AuthResult<Unit>>()
    val otpResults = resultChannel.receiveAsFlow()

    init {
        savedStateHandle.get<String>(REGISTER_DATA_ARGUMENT_KEY)?.let { registerDataJson ->
            val registerData = registerDataJson.fromJson(RegisterData::class.java)
            state = state.copy(
                signUpFirstName = registerData.firstName,
                signUpLastName = registerData.lastName,
                signUpPhoneNumber = registerData.phoneNumber,
                signUpPassword = registerData.password
            )
        }
    }

    fun onEvent(event: OtpUiEvent) {
        when (event) {
            is OtpUiEvent.OtpCodeChanged -> state = state.copy(otp = event.value)
            is OtpUiEvent.VerifyOtpAndSignUp -> verifyOtpAndSignUp()
        }
    }

    private fun verifyOtpAndSignUp() {

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
                    CreateAccountRequest(
                        firstName = state.signUpFirstName,
                        lastName = state.signUpLastName,
                        phoneNumber = state.signUpPhoneNumber,
                        password = state.signUpPassword
                    )
                )
                context.dataStore.updateData { profile ->
                    profile.copy(
                        firstName = state.signUpFirstName,
                        lastName = state.signUpLastName,
                        phoneNumber = state.signUpPhoneNumber
                    )
                }
                resultChannel.send(signUpResult)
            } else {
                resultChannel.send(AuthResult.IncorrectOtp())
            }
            state = state.copy(isLoading = false)
        }
    }

    fun resendCode(context: Context) {
        viewModelScope.launch {
            val resendResult = repository.sendOtp(
                phoneNumber = state.signUpPhoneNumber,
                activity = context.findActivity(),
                isResendAction = true
            )
            resultChannel.send(resendResult)
        }
    }
}