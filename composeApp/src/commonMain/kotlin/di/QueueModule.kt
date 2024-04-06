package di

import com.benasher44.uuid.Uuid
import org.koin.dsl.module
import queue.data.repository.QueueRepositoryImpl
import queue.data.source.remote.QueueRemoteDataSource
import queue.data.source.remote.QueueRemoteDataSourceImpl
import queue.domain.model.Line
import queue.domain.repository.QueueRepository
import queue.domain.use_case.CloseSessionUseCase
import queue.presentation.admin.QueueAdminViewModel
import queue.presentation.details.QueueItemDetailsViewModel
import queue.presentation.list.QueueViewModel

val queueModule = module {
    single<QueueRemoteDataSource> { QueueRemoteDataSourceImpl(get()) }
    single<QueueRepository> { QueueRepositoryImpl(get()) }
    single { QueueViewModel(get()) }

    factory { CloseSessionUseCase(get()) }

    factory { (userId: Uuid) ->
        QueueItemDetailsViewModel(get(), userId)
    }

    factory { (line: Line) ->
        QueueAdminViewModel(get(), get(), line)
    }
}