package core.domain.use_case.validation

import core.domain.model.ValidationError
import core.domain.utils.Result

class ValidateLastNameUseCase {

    operator fun invoke(lastName: String): Result<Unit, ValidationError> {
        if (lastName.isEmpty()) {
            return Result.Error(error = ValidationError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}