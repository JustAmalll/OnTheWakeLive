package paywall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import paywall.domain.repository.PaywallRepository
import paywall.presentation.PaywallEvent.OnReceiptSelected

class PaywallViewModel(
    private val paywallRepository: PaywallRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PaywallState())
    val state: StateFlow<PaywallState> = _state.asStateFlow()

    private val _action = Channel<PaywallAction>()
    val actions: Flow<PaywallAction> = _action.receiveAsFlow()

    fun onEvent(event: PaywallEvent) {
        when (event) {
            PaywallEvent.OnSelectReceiptClicked -> viewModelScope.launch {
                _action.send(PaywallAction.OpenFilePicker)
            }

            is OnReceiptSelected -> _state.update { it.copy(receipt = event.receipt) }
            PaywallEvent.OnSubmitClicked -> sendReceipt()
        }
    }

    private fun sendReceipt() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            paywallRepository.sentReceipt(receipt = state.value.receipt ?: return@launch)
            _state.update { it.copy(isLoading = false) }
        }
    }

    sealed interface PaywallAction {
        data object OpenFilePicker : PaywallAction
    }
}