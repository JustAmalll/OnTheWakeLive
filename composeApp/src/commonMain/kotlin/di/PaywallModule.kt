package di

import org.koin.dsl.module
import paywall.data.repository.PaywallRepositoryImpl
import paywall.data.source.remote.PaywallRemoteDataSource
import paywall.data.source.remote.PaywallRemoteDataSourceImpl
import paywall.domain.repository.PaywallRepository
import paywall.presentation.PaywallViewModel

val paywallModule = module {
    factory<PaywallRemoteDataSource> { PaywallRemoteDataSourceImpl(get()) }
    factory<PaywallRepository> { PaywallRepositoryImpl(get()) }
    factory { PaywallViewModel(get()) }
}