package di

import org.koin.dsl.module
import paywall.data.repository.PaywallRepositoryImpl
import paywall.data.source.cache.PaywallCacheDataSource
import paywall.data.source.cache.PaywallCacheDataSourceImpl
import paywall.data.source.remote.PaywallRemoteDataSource
import paywall.data.source.remote.PaywallRemoteDataSourceImpl
import paywall.domain.repository.PaywallRepository
import paywall.presentation.form.PaywallViewModel
import paywall.presentation.in_processing.PaywallInProcessingViewModel

val paywallModule = module {
    factory<PaywallCacheDataSource> { PaywallCacheDataSourceImpl(get()) }
    factory<PaywallRemoteDataSource> { PaywallRemoteDataSourceImpl(get()) }
    factory<PaywallRepository> { PaywallRepositoryImpl(get(), get()) }
    factory { PaywallViewModel(get()) }
    factory { PaywallInProcessingViewModel(get(), get(), get()) }
}