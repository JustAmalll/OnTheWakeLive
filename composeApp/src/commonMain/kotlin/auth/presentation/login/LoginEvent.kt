package auth.presentation.login

sealed interface LoginEvent {
    data class OnPhoneNumberChanged(val value: String) : LoginEvent
    data class OnPasswordChanged(val value: String) : LoginEvent
    data object OnTogglePasswordVisibilityClicked: LoginEvent
    data object OnCreateAccountClicked : LoginEvent
    data object OnSignInClicked : LoginEvent
}