package di

import core.domain.use_case.validation.ValidateFirstNameUseCase
import core.domain.use_case.validation.ValidateLastNameUseCase
import core.domain.use_case.validation.ValidatePasswordUseCase
import core.domain.use_case.validation.ValidatePhoneNumberUseCase
import org.koin.dsl.module

val validationModule = module {
    factory { ValidateLastNameUseCase() }
    factory { ValidateFirstNameUseCase() }
    factory { ValidatePasswordUseCase() }
    factory { ValidatePhoneNumberUseCase() }
}