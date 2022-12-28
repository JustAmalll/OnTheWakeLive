package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.core.util.Constants.PREFS_USER_ID
import com.onthewake.onthewakelive.core.util.Resource
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
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

    var state by mutableStateOf(AddTrickListState())

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    val userId = prefs.getString(PREFS_USER_ID, null)

    init {
        if (userId != null) getUsersTrickList(userId)
        getAllTrickList()
    }

    private fun getAllTrickList() {
        viewModelScope.launch {
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
            when (val result = trickListRepository.getUsersTrickList(userId)) {
                is Resource.Success -> {
                    state = state.copy(userTrickList = result.data ?: TrickList())
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(result.message ?: "Unknown Error")
                }
            }
        }
    }

    fun addTrick(trickList: TrickList) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = trickListRepository.addTrickList(trickList)) {
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