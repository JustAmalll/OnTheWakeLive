package paywall.presentation.in_processing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.utils.onFailure
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import paywall.domain.repository.PaywallRepository
import user_profile.domain.use_case.GetUserProfileUseCase
import user_profile.domain.use_case.IsUserSubscribedUseCase
import kotlin.time.Duration.Companion.seconds

class PaywallInProcessingViewModel(
    private val paywallRepository: PaywallRepository,
    private val isUserSubscribed: IsUserSubscribedUseCase,
    private val getUserProfile: GetUserProfileUseCase
) : ViewModel() {

    private val _action = Channel<PaywallInProcessingAction>()
    val actions: Flow<PaywallInProcessingAction> = _action.receiveAsFlow()

    init {
        viewModelScope.launch {
            paywallRepository.setPaywallInProcessingState(isProcessing = true)
        }
        viewModelScope.launch {
            val userId = getUserProfile().getOrNull()?.userId ?: run {
                _action.send(PaywallInProcessingAction.NavigateToPaywallFailureScreen)
                return@launch
            }
            delay(20.seconds)

            while (
                isUserSubscribed(userId = userId).onFailure {
                    _action.send(PaywallInProcessingAction.NavigateToPaywallFailureScreen)
                    return@launch
                }.getOrNull() != true
            ) {
                delay(8.seconds)
            }
            paywallRepository.setPaywallInProcessingState(isProcessing = false)
            _action.send(PaywallInProcessingAction.NavigateToPaywallSuccessScreen)
        }
    }

    sealed interface PaywallInProcessingAction {
        data object NavigateToPaywallSuccessScreen : PaywallInProcessingAction
        data object NavigateToPaywallFailureScreen : PaywallInProcessingAction
    }
}