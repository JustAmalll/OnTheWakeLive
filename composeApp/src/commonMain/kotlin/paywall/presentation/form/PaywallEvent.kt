package paywall.presentation.form

sealed interface PaywallEvent {
    @Suppress("ArrayInDataClass")
    data class OnReceiptSelected(val receipt: ByteArray): PaywallEvent

    data object OnSubmitClicked: PaywallEvent
}