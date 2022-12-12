package com.onthewake.onthewakelive.feature_full_size_avatar.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.onthewake.onthewakelive.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullSizeAvatarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var profileUrl by mutableStateOf("")

    init {
        savedStateHandle.get<String>(Constants.PICTURE_URL_ARGUMENT_KEY)?.let { url ->
            profileUrl = url
        }
    }
}