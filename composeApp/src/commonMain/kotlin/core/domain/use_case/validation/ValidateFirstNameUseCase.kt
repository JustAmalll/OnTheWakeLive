package core.domain.use_case.validation

import core.domain.model.ValidationError
import core.domain.utils.Result

class ValidateFirstNameUseCase {

    operator fun invoke(firstName: String): Result<Unit, ValidationError> {
        if (firstName.isEmpty()) {
            return Result.Error(error = ValidationError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}