package queue.presentation.admin

import queue.domain.model.Line
import user_profile.domain.model.UserProfile

sealed interface QueueAdminEvent {
    data object OnNavigateBackClicked: QueueAdminEvent
    data class OnFirstNameChanged(val value: String) : QueueAdminEvent
    data class OnUserSelected(val userProfile: UserProfile): QueueAdminEvent
    data class OnLineSelected(val line: Line): QueueAdminEvent
    data object OnAddUserClicked: QueueAdminEvent
    data object OnChangeSelectedUserClicked: QueueAdminEvent
}