package queue.domain.use_case

import queue.domain.repository.QueueRepository

class UpdateNotificationTokenUseCase(private val queueRepository: QueueRepository) {

    suspend operator fun invoke(newToken: String) {
        queueRepository.updateNotificationToken(newToken = newToken)
    }
}