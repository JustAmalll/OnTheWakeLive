package com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class TrickListState(
    val isLoading: Boolean = false,
    val userTrickList: TrickList? = null
)