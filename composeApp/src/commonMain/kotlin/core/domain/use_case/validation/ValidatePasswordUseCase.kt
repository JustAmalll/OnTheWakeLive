package core.domain.use_case.validation

import core.domain.model.ValidationError
import core.domain.utils.Result
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.empty_field_error
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ValidatePasswordUseCase {

    operator fun invoke(password: String): Result<Unit, ValidationError> {
        if (password.isEmpty()) {
            return Result.Error(error = ValidationError.EMPTY_FIELD)
        }
        return Result.Success(Unit)
    }
}