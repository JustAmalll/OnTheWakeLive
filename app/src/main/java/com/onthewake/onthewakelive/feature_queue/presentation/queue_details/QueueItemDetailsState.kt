package com.onthewake.onthewakelive.feature_queue.presentation.queue_details

data class QueueItemDetailsState(
    val isLoading: Boolean = false,
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val profilePictureUri: String = "",
    val telegram: String = "",
    val instagram: String = "",
    val dateOfBirth: String = ""
)