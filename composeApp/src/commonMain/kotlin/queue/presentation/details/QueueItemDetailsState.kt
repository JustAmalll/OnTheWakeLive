package queue.presentation.details

import androidx.compose.runtime.Stable
import user_profile.domain.model.UserProfile

@Stable
data class QueueItemDetailsState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null
)