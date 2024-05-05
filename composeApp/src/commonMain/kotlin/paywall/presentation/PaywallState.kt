package paywall.presentation

import androidx.compose.runtime.Stable

@Suppress("ArrayInDataClass")
@Stable
data class PaywallState(
    val isLoading: Boolean = false,
    val receipt: ByteArray? = null
)