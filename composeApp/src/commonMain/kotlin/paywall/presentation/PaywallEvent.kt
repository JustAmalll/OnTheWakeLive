package paywall.presentation

sealed interface PaywallEvent {
    data object OnSelectReceiptClicked: PaywallEvent

    @Suppress("ArrayInDataClass")
    data class OnReceiptSelected(val receipt: ByteArray): PaywallEvent

    data object OnSubmitClicked: PaywallEvent
}