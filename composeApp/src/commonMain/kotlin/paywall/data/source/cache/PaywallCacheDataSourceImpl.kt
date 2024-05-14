package paywall.data.source.cache

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PaywallCacheDataSourceImpl(
    private val observableSettings: ObservableSettings
) : PaywallCacheDataSource {

    override suspend fun setPaywallInProcessingState(isProcessing: Boolean) {
        withContext(Dispatchers.IO) {
            observableSettings.putBoolean(key = PAYWALL_IN_PROCESSING_STATE, value = isProcessing)
        }
    }

    override suspend fun isPaymentInProcessing(): Boolean =
        observableSettings.getBoolean(key = PAYWALL_IN_PROCESSING_STATE, defaultValue = false)

    private companion object {
        private const val PAYWALL_IN_PROCESSING_STATE = "paywall_in_processing_state"
    }
}