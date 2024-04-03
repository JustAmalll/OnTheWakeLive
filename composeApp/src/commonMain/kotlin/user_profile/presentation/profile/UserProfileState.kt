package user_profile.presentation.profile

import androidx.compose.runtime.Stable
import user_profile.domain.model.UserProfile

@Stable
data class UserProfileState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null
)