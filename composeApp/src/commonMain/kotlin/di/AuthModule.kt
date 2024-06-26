package di

import auth.data.repository.AuthRepositoryImpl
import auth.data.source.cache.AuthCacheDataSource
import auth.data.source.cache.AuthCacheDataSourceImpl
import auth.data.source.remote.AuthRemoteDataSource
import auth.data.source.remote.AuthRemoteDataSourceImpl
import auth.domain.repository.AuthRepository
import auth.domain.use_case.AuthenticationUseCase
import auth.domain.use_case.CreateAccountUseCase
import auth.domain.use_case.GetUserIdUseCase
import auth.domain.use_case.IsUserAdminUseCase
import auth.domain.use_case.IsUserAlreadyExistsUseCase
import auth.domain.use_case.LoginUseCase
import auth.domain.use_case.LogoutUseCase
import auth.presentation.create_account.CreateAccountViewModel
import auth.presentation.login.LoginViewModel
import org.koin.dsl.module

val authModule = module {
    factory<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    factory<AuthCacheDataSource> { AuthCacheDataSourceImpl(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    factory { AuthenticationUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { CreateAccountUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { IsUserAdminUseCase(get()) }
    factory { GetUserIdUseCase(get()) }
    factory { IsUserAlreadyExistsUseCase(get()) }

    factory { LoginViewModel(get(), get(), get()) }
    factory { CreateAccountViewModel(get(), get(), get(), get(), get(), get()) }
}