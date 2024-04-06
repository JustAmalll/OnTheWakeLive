package queue.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.utils.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import queue.domain.model.Line
import queue.domain.repository.QueueRepository
import queue.presentation.admin.QueueAdminEvent.OnAddUserClicked
import queue.presentation.admin.QueueAdminEvent.OnChangeSelectedUserClicked
import queue.presentation.admin.QueueAdminEvent.OnFirstNameChanged
import queue.presentation.admin.QueueAdminEvent.OnLineSelected
import queue.presentation.admin.QueueAdminEvent.OnUserSelected
import user_profile.domain.use_case.SearchUsersUseCase

class QueueAdminViewModel(
    private val queueRepository: QueueRepository,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val line: Line
) : ViewModel() {

    private val _state = MutableStateFlow(QueueAdminState())
    val state: StateFlow<QueueAdminState> = _state.asStateFlow()

    private val _action = Channel<QueueAdminAction>()
    val actions: Flow<QueueAdminAction> = _action.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        _state.update { it.copy(line = line) }
    }

    fun onEvent(event: QueueAdminEvent) {
        when (event) {
            is OnFirstNameChanged -> {
                _state.update { it.copy(firstName = event.value) }
                searchUser(searchQuery = event.value)
            }

            is OnUserSelected -> _state.update { it.copy(selectedUser = event.userProfile) }
            is OnLineSelected -> _state.update { it.copy(line = event.line) }

            OnChangeSelectedUserClicked -> _state.update {
                it.copy(selectedUser = null, firstName = "", searchedUsers = persistentListOf())
            }

            QueueAdminEvent.OnNavigateBackClicked -> viewModelScope.launch {
                _action.send(QueueAdminAction.NavigateBack)
            }

            OnAddUserClicked -> addUser()
        }
    }

    private fun addUser() {
        viewModelScope.launch {
            queueRepository.adminAddUserToTheQueue(
                userId = state.value.selectedUser?.userId,
                line = state.value.line,
                fullName = state.value.selectedUser?.firstName ?: state.value.firstName
            ).onSuccess {
                _action.send(QueueAdminAction.NavigateBack)
            }
        }
    }

    private fun searchUser(searchQuery: String) {
        if (searchQuery.isEmpty()) return
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _state.update { it.copy(isUserSearching = true) }
            delay(100L)

            searchUsersUseCase(searchQuery = searchQuery).onSuccess { searchedUsers ->
                _state.update { it.copy(searchedUsers = searchedUsers.toImmutableList()) }
            }
            _state.update { it.copy(isUserSearching = false) }
        }
    }


    sealed interface QueueAdminAction {
        data object NavigateBack : QueueAdminAction
    }
}