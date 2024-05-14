package paywall.data.repository

import core.domain.utils.DataError
import core.domain.utils.Result
import paywall.data.source.cache.PaywallCacheDataSource
import paywall.data.source.remote.PaywallRemoteDataSource
import paywall.domain.repository.PaywallRepository

class PaywallRepositoryImpl(
    private val paywallRemoteDataSource: PaywallRemoteDataSource,
    private val paywallCacheDataSource: PaywallCacheDataSource,
) : PaywallRepository {

    override suspend fun sentReceipt(receipt: ByteArray): Result<Unit, DataError.Network> =
        paywallRemoteDataSource.sentReceipt(receipt = receipt)

    override suspend fun setPaywallInProcessingState(isProcessing: Boolean) =
        paywallCacheDataSource.setPaywallInProcessingState(isProcessing = isProcessing)

    override suspend fun isPaymentInProcessing(): Boolean =
        paywallCacheDataSource.isPaymentInProcessing()
}