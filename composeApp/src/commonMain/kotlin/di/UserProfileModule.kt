package di

import activate_subscription.ActivateSubscriptionViewModel
import org.koin.dsl.module
import user_profile.data.repository.UserProfileRepositoryImpl
import user_profile.data.source.cache.UserProfileCacheDataSource
import user_profile.data.source.cache.UserProfileCacheDataSourceImpl
import user_profile.data.source.remote.UserProfileRemoteDataSource
import user_profile.data.source.remote.UserProfileRemoteDataSourceImpl
import user_profile.domain.repository.UserProfileRepository
import user_profile.domain.use_case.ActivateSubscriptionUseCase
import user_profile.domain.use_case.EditProfileUseCase
import user_profile.domain.use_case.GetQueueItemDetailsUseCase
import user_profile.domain.use_case.GetUserProfileUseCase
import user_profile.domain.use_case.IsUserSubscribedUseCase
import user_profile.domain.use_case.SearchUsersUseCase
import user_profile.presentation.edit_profile.EditUserProfileViewModel
import user_profile.presentation.profile.UserProfileViewModel

val userProfileModule = module {
    factory<UserProfileRemoteDataSource> { UserProfileRemoteDataSourceImpl(get()) }
    factory<UserProfileCacheDataSource> { UserProfileCacheDataSourceImpl(get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get(), get()) }

    factory { GetUserProfileUseCase(get()) }
    factory { EditProfileUseCase(get()) }
    factory { GetQueueItemDetailsUseCase(get()) }
    factory { SearchUsersUseCase(get()) }
    factory { IsUserSubscribedUseCase(get()) }
    factory { ActivateSubscriptionUseCase(get()) }

    factory { UserProfileViewModel(get(), get(), get()) }
    factory { EditUserProfileViewModel(get(), get()) }
    factory { ActivateSubscriptionViewModel(get(), get()) }
}