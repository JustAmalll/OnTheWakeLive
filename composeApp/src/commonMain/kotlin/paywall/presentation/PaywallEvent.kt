package paywall.presentation

sealed interface PaywallEvent {
    @Suppress("ArrayInDataClass")
    data class OnReceiptSelected(val receipt: ByteArray): PaywallEvent

    data object OnSubmitClicked: PaywallEvent
}