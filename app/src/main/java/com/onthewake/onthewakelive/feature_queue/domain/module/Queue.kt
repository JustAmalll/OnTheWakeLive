package com.onthewake.onthewakelive.feature_queue.domain.module

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val isLeftQueue: Boolean,
    val timestamp: Long
)