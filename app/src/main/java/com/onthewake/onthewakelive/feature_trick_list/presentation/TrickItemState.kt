package com.onthewake.onthewakelive.feature_trick_list.presentation

import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.TrickItemResponse

data class TrickItemState (
    val name: String,
    val description: String,
    val isSelected: Boolean = false
)

fun TrickItemState.toTrickItemResponse(): TrickItemResponse = TrickItemResponse(
    name = name, description = description
)