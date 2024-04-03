package user_profile.presentation.edit_profile

import androidx.compose.runtime.Stable

@Stable
data class EditUserProfileState(
    val isLoading: Boolean = false,

    val firstName: String = "",
    val firsNameError: String? = null,

    val lastName: String = "",
    val lastNameError: String? = null,

    val phoneNumber: String = "",
    val telegram: String = "",
    val instagram: String = "",
    val photo: ByteArray? = null
)