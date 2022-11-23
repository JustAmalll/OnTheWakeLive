package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickListDto
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
import com.onthewake.onthewakelive.feature_trick_list.presentation.TrickListState
import com.onthewake.onthewakelive.util.Constants
import com.onthewake.onthewakelive.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTricksViewModel @Inject constructor(
    private val trickListRepository: TrickListRepository,
    prefs: SharedPreferences
) : ViewModel() {

    var state by mutableStateOf(TrickListState())

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    val userId = prefs.getString(Constants.PREFS_USER_ID, null)

    init {
        if (userId != null) getUsersTrickList(userId)
        getAllTrickList()
    }

    private fun getAllTrickList() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            trickListRepository.getTrickList().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        state = state.copy(allTrickList = result.data)
                    }
                    is Resource.Error -> {
                        _snackBarEvent.emit(result.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    private fun getUsersTrickList(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = trickListRepository.getUsersTrickList(userId)
            state = when (result) {
                is Resource.Success -> {
                    state.copy(userTrickList = result.data ?: TrickList())
                }
                is Resource.Error -> {
                    state.copy(userTrickList = TrickList())
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    fun addTrick(trickListDto: TrickListDto) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = trickListRepository.addTrickList(trickListDto)) {
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