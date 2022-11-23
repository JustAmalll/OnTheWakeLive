package com.onthewake.onthewakelive.feature_trick_list.domain.model

@kotlinx.serialization.Serializable
data class TrickList(
    val spins: List<TrickItem> = emptyList(),
    val raileyTricks: List<TrickItem> = emptyList(),
    val backRollTricks: List<TrickItem> = emptyList(),
    val frontFlipTricks: List<TrickItem> = emptyList(),
    val frontRollTricks: List<TrickItem> = emptyList(),
    val tantrumTricks: List<TrickItem> = emptyList(),
    val whipTricks: List<TrickItem> = emptyList(),
    val grabs: List<TrickItem> = emptyList(),
    val rails: List<TrickItem> = emptyList()
)
