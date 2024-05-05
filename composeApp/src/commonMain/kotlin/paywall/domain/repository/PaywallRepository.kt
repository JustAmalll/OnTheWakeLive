package paywall.domain.repository

import core.domain.utils.DataError
import core.domain.utils.Result

interface PaywallRepository {
    suspend fun sentReceipt(receipt: ByteArray): Result<Unit, DataError.Network>
}