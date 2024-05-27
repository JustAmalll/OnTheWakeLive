package activate_subscription

import activate_subscription.ActivateSubscriptionEvent.OnActivateSubscriptionClicked
import activate_subscription.ActivateSubscriptionEvent.OnUserPhotoClicked
import activate_subscription.ActivateSubscriptionEvent.OnUserSelected
import activate_subscription.ActivateSubscriptionViewModel.ActivateSubscriptionAction.NavigateToFullSizePhotoScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.utils.asString
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import user_profile.domain.use_case.ActivateSubscriptionUseCase
import user_profile.domain.use_case.SearchUsersUseCase

class ActivateSubscriptionViewModel(
    private val searchUsers: SearchUsersUseCase,
    private val activateSubscription: ActivateSubscriptionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ActivateSubscriptionState())
    val state: StateFlow<ActivateSubscriptionState> = _state.asStateFlow()

    private val _action = Channel<ActivateSubscriptionAction>()
    val actions: Flow<ActivateSubscriptionAction> = _action.receiveAsFlow()

    fun onEvent(event: ActivateSubscriptionEvent) {
        when (event) {
            ActivateSubscriptionEvent.OnChangeSelectedUserClicked -> _state.update {
                it.copy(selectedUser = null, searchQuery = "", searchedUsers = persistentListOf())
            }

            ActivateSubscriptionEvent.OnNavigateBackClicked -> viewModelScope.launch {
                _action.send(ActivateSubscriptionAction.NavigateBack)
            }

            is ActivateSubscriptionEvent.OnSearchQueueChanged -> {
                _state.update { it.copy(searchQuery = event.value) }
                searchUser(searchQuery = event.value)
            }

            is OnUserPhotoClicked -> viewModelScope.launch {
                _action.send(NavigateToFullSizePhotoScreen(photo = event.photo))
            }

            is OnUserSelected -> _state.update { it.copy(selectedUser = event.user) }
            OnActivateSubscriptionClicked -> activateSubscription()
        }
    }

    private fun searchUser(searchQuery: String) {
        if (searchQuery.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isUserSearching = true) }

            searchUsers(searchQuery = searchQuery).onSuccess { searchedUsers ->
                _state.update { it.copy(searchedUsers = searchedUsers.toImmutableList()) }
            }
            _state.update { it.copy(isUserSearching = false) }
        }
    }

    private fun activateSubscription() {
        viewModelScope.launch {
            val userId = state.value.selectedUser?.userId ?: return@launch

            activateSubscription(userId = userId).onSuccess {
                _action.send(ActivateSubscriptionAction.NavigateBack)
            }.onFailure { error ->
                _action.send(ActivateSubscriptionAction.ShowError(error.asString()))
            }
        }
    }

    sealed interface ActivateSubscriptionAction {
        data object NavigateBack : ActivateSubscriptionAction
        data class NavigateToFullSizePhotoScreen(val photo: String) : ActivateSubscriptionAction
        data class ShowError(val message: String) : ActivateSubscriptionAction
    }
}