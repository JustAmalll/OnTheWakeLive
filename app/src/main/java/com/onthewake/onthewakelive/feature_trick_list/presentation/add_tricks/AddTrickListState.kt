package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class AddTrickListState(
    val isLoading: Boolean = false,
    val allTrickList: TrickList? = null,
    val userTrickList: TrickList? = null
)