package core.presentation

import androidx.compose.runtime.Stable


@Stable
data class MainState(
    val isLoading: Boolean = false,
    val isUserAdmin: Boolean = false,
    val userId: Int? = null
)