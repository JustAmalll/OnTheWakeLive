package core.domain.use_case.validation

import core.domain.model.ValidationError
import core.domain.utils.Result

class ValidatePasswordUseCase {

    operator fun invoke(password: String): Result<Unit, ValidationError> {
        if (password.isEmpty()) {
            return Result.Error(error = ValidationError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}