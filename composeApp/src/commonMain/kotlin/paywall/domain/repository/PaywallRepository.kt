package paywall.domain.repository

import core.domain.utils.DataError
import core.domain.utils.Result

interface PaywallRepository {
    suspend fun sentReceipt(receipt: ByteArray): Result<Unit, DataError.Network>
    suspend fun setPaywallInProcessingState(isProcessing: Boolean)
    suspend fun isPaymentInProcessing(): Boolean
}