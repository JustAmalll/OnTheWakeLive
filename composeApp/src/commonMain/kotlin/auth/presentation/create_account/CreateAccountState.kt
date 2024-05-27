package auth.presentation.create_account

data class CreateAccountState(
    val isLoading: Boolean = false,
    val showPassword: Boolean = false,

    val firstName: String = "",
    val firstNameError: String? = null,

    val lastName: String = "",
    val lastNameError: String? = null,

    val phoneNumber: String = "",
    val phoneNumberError: String? = null,

    val password: String = "",
    val passwordError: String? = null
)