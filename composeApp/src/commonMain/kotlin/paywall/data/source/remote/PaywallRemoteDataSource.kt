package paywall.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result

interface PaywallRemoteDataSource {
    suspend fun sentReceipt(receipt: ByteArray): Result<Unit, DataError.Network>
}