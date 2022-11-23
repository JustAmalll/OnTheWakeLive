package com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class TrickListViewModel @Inject constructor(
    private val trickListRepository: TrickListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(TrickListState())

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        savedStateHandle.get<String>(Constants.USER_ID_ARGUMENT_KEY)?.let { userId ->
            getUsersTrickList(userId)
        }
    }

    private fun getUsersTrickList(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = trickListRepository.getUsersTrickList(userId)) {
                is Resource.Success -> {
                    state = state.copy(allTrickList = result.data)
                }
                is Resource.Error -> {}
            }
            state = state.copy(isLoading = false)
        }
    }
}