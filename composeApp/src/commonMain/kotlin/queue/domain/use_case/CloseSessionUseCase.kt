package queue.domain.use_case

import queue.domain.repository.QueueRepository

class CloseSessionUseCase(private val queueRepository: QueueRepository) {

    suspend operator fun invoke() {
        queueRepository.closeSession()
    }
}