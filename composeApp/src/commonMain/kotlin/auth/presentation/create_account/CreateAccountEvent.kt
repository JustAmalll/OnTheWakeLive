package auth.presentation.create_account

sealed interface CreateAccountEvent {
    data class OnFirstNameChanged(val value: String) : CreateAccountEvent
    data class OnLastNameChanged(val value: String) : CreateAccountEvent
    data class OnPhoneNumberChanged(val value: String) : CreateAccountEvent
    data class OnPasswordChanged(val value: String) : CreateAccountEvent

    data object OnCreateAccountClicked: CreateAccountEvent
    data object OnLoginClicked: CreateAccountEvent
}