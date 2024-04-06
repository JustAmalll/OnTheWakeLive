package core.presentation

import androidx.compose.runtime.Stable
import com.benasher44.uuid.Uuid

@Stable
data class MainState(
    val isLoading: Boolean = false,
    val isUserAdmin: Boolean = false,
    val userId: Uuid? = null
)