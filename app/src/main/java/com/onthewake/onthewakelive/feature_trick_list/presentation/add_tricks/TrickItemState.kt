package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickItem

data class TrickItemState(
    val name: String,
    val description: String,
    val isSelected: Boolean = false
)

fun TrickItemState.toTrickItem(): TrickItem = TrickItem(
    name = name, description = description
)