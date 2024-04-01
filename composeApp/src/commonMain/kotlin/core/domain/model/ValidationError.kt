package core.domain.model

import core.domain.utils.Error
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.empty_field_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

enum class ValidationError: Error {
    EMPTY_FIELD
}

@OptIn(ExperimentalResourceApi::class)
suspend fun ValidationError.asString(): String = when(this) {
    ValidationError.EMPTY_FIELD -> getString(resource = Res.string.empty_field_error)
}