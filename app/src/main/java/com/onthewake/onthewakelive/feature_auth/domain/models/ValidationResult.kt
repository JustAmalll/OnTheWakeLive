package com.onthewake.onthewakelive.feature_auth.domain.models

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)