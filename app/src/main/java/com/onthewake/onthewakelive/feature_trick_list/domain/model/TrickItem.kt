package com.onthewake.onthewakelive.feature_trick_list.domain.model

import com.onthewake.onthewakelive.feature_trick_list.presentation.TrickItemState

@kotlinx.serialization.Serializable
data class TrickItem(
    val name: String,
    val description: String
)

fun TrickItem.toTrickItemState(): TrickItemState = TrickItemState(
    name = name, description = description, isSelected = false
)