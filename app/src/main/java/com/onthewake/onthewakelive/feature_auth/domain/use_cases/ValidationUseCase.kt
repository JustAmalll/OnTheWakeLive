package com.onthewake.onthewakelive.feature_auth.domain.use_cases

import android.content.Context
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import java.util.*

class ValidationUseCase(
    private val context: Context
) {

    fun validateFirstName(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_first_name_error)
            )
        }
        return ValidationResult(successful = true)
    }

    fun validateAdminAddToQueue(firstName: String, queue: List<Queue>): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_first_name_error)
            )
        }
        if (queue.toString().contains(firstName)) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_user_error)
            )
        }
        return ValidationResult(successful = true)
    }

    fun validateLastName(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_last_name_error)
            )
        }
        return ValidationResult(successful = true)
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_phone_number_error)
            )
        }
//        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
//            return ValidationResult(
//                successful = false,
//                errorMessage = context.getString(R.string.validate_invalid_phone_number_error)
//            )
//        }
        return ValidationResult(successful = true)
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_password_error)
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_password_length_error)
            )
        }
        return ValidationResult(successful = true)
    }

    fun validateDateOfBirth(dateOfBirth: String): ValidationResult {
        if (dateOfBirth.isNotEmpty()) {
            if (dateOfBirth.takeLast(4) >= Calendar.getInstance().get(Calendar.YEAR).toString()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = context.getString(R.string.validate_date_of_birth_error)
                )
            }
            if (dateOfBirth.take(2).toInt() > 31) {
                return ValidationResult(
                    successful = false,
                    errorMessage = context.getString(R.string.validate_date_of_birth_day_error)
                )
            }
            if (dateOfBirth.drop(2).dropLast(4).toInt() > 12) {
                return ValidationResult(
                    successful = false,
                    errorMessage = context.getString(R.string.validate_date_of_birth_month_error)
                )
            }
        }
        return ValidationResult(successful = true)
    }

    fun validateOtp(otp: String): ValidationResult {
        if (otp.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_otp_error)
            )
        }
        if (otp.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_otp_error)
            )
        }
        return ValidationResult(successful = true)
    }
}