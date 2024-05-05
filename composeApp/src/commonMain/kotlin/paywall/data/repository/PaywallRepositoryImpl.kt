package paywall.data.repository

import core.domain.utils.DataError
import core.domain.utils.Result
import paywall.data.source.remote.PaywallRemoteDataSource
import paywall.domain.repository.PaywallRepository

class PaywallRepositoryImpl(
    private val paywallRemoteDataSource: PaywallRemoteDataSource
) : PaywallRepository {

    override suspend fun sentReceipt(receipt: ByteArray): Result<Unit, DataError.Network> =
        paywallRemoteDataSource.sentReceipt(receipt = receipt)
}