package com.onthewake.onthewakelive.feature_trick_list.data.remote.dto

@kotlinx.serialization.Serializable
data class TrickItem(
    val name: String,
    val description: String
)