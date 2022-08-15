package com.onthewake.onthewakelive.feature_auth.domain.use_cases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)