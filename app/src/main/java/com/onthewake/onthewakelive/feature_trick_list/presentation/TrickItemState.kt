package com.onthewake.onthewakelive.feature_trick_list.presentation

import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickItemDto

data class TrickItemState (
    val name: String,
    val description: String,
    val isSelected: Boolean = false
)

fun TrickItemState.toTrickItemDto(): TrickItemDto = TrickItemDto(
    name = name, description = description
)