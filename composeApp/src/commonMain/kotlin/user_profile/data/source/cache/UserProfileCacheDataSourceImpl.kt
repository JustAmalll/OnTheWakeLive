package user_profile.data.source.cache

import com.russhwolf.settings.ObservableSettings
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingLocal
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import user_profile.domain.model.UserProfile

class UserProfileCacheDataSourceImpl(
    private val observableSettings: ObservableSettings
) : UserProfileCacheDataSource {

    override suspend fun cacheUserProfile(userProfile: UserProfile) {
        runCatchingLocal {
            observableSettings.putString(
                key = USER_PROFILE,
                value = Json.encodeToString(userProfile)
            )
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile?, DataError.Local> =
        runCatchingLocal {
            observableSettings.getStringOrNull(key = USER_PROFILE)?.let {
                Json.decodeFromString(string = it)
            }
        }

    companion object {
        private const val USER_PROFILE = "user_profile"
    }
}