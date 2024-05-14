package paywall.data.source.cache

interface PaywallCacheDataSource {
    suspend fun setPaywallInProcessingState(isProcessing: Boolean)
    suspend fun isPaymentInProcessing(): Boolean
}