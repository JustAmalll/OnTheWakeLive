package di

import org.koin.dsl.module
import queue.data.repository.QueueRepositoryImpl
import queue.data.source.remote.QueueRemoteDataSource
import queue.data.source.remote.QueueRemoteDataSourceImpl
import queue.domain.repository.QueueRepository
import queue.presentation.QueueViewModel

val queueModule = module {
    single<QueueRemoteDataSource> { QueueRemoteDataSourceImpl(get()) }
    single<QueueRepository> { QueueRepositoryImpl(get()) }
    single { QueueViewModel(get()) }
}