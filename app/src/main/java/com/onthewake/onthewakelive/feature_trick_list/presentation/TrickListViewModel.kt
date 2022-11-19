package com.onthewake.onthewakelive.feature_trick_list.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.TrickListResponse
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
import com.onthewake.onthewakelive.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrickListViewModel @Inject constructor(
    private val trickListRepository: TrickListRepository
) : ViewModel() {

    var state by mutableStateOf(TrickListState())

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        getTrickList()
    }

    private fun getTrickList() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = trickListRepository.getTrickList()) {
                is Resource.Success -> {
                    result.data?.let { trickList ->
                        state = state.copy(trickList = trickList)
                    }
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(result.message ?: "Unknown Error")
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    fun addTrickList(trickListResponse: TrickListResponse) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = trickListRepository.addTrickList(trickListResponse)) {
                is Resource.Success -> {
                    _snackBarEvent.emit("Success!")
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(result.message ?: "Unknown Error")
                }
            }
            state = state.copy(isLoading = false)
        }
    }

}