package com.onthewake.onthewakelive.feature_splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val resultChannel = Channel<AuthResult>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = repository.authenticate()
            delay(1200)
            resultChannel.send(result)
        }
    }
}