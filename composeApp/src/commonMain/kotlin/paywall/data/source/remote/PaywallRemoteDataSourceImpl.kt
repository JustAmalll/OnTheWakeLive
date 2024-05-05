package paywall.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingNetwork
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PaywallRemoteDataSourceImpl(
    private val client: HttpClient
) : PaywallRemoteDataSource {

    override suspend fun sentReceipt(
        receipt: ByteArray
    ): Result<Unit, DataError.Network> = runCatchingNetwork {
        client.post("/purchase-subscription") {
            setBody(receipt)
        }
    }
}