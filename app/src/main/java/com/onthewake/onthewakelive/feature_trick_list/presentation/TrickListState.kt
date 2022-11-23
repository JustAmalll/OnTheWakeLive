package com.onthewake.onthewakelive.feature_trick_list.presentation

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class TrickListState(
    val isLoading: Boolean = false,
    val allTrickList: TrickList? = null,
    val userTrickList: TrickList? = null
)