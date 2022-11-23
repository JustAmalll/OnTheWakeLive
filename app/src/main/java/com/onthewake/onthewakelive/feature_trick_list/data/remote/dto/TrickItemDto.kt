package com.onthewake.onthewakelive.feature_trick_list.data.remote.dto

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickItem

data class TrickItemDto(
    val name: String,
    val description: String
)

fun TrickItemDto.toTrickItem(): TrickItem = TrickItem(
    name = name, description = description
)