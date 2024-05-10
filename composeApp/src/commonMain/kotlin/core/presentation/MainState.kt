package core.presentation

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.screen.Screen

@Stable
data class MainState(
    val isLoading: Boolean = false,
    val startScreen: Screen? = null,
    val isUserAdmin: Boolean = false,
    val userId: Int? = null
)