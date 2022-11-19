package com.onthewake.onthewakelive.feature_trick_list.data.remote.response

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickItem

data class TrickItemResponse(
    val name: String,
    val description: String
)

fun TrickItemResponse.toTrickItem(): TrickItem = TrickItem(
    name = name, description = description
)