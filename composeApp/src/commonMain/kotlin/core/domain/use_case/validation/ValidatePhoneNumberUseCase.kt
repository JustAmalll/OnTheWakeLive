package core.domain.use_case.validation

import core.domain.model.ValidationError
import core.domain.utils.Result

class ValidatePhoneNumberUseCase {

    operator fun invoke(phoneNumber: String): Result<Unit, ValidationError> {
        if (phoneNumber.isEmpty()) {
            return Result.Error(error = ValidationError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}