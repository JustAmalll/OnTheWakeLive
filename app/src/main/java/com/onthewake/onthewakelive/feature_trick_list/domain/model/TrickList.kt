package com.onthewake.onthewakelive.feature_trick_list.domain.model

@kotlinx.serialization.Serializable
data class TrickList(
    val spins: List<TrickItem>,
    val raileyTricks: List<TrickItem>,
    val backRollTricks: List<TrickItem>,
    val frontFlipTricks: List<TrickItem>,
    val frontRollTricks: List<TrickItem>,
    val tantrumTricks: List<TrickItem>,
    val whipTricks: List<TrickItem>,
    val grabs: List<TrickItem>,
    val rails: List<TrickItem>
)
