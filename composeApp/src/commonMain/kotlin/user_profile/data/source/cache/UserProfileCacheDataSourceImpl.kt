package user_profile.data.source.cache

import com.russhwolf.settings.ObservableSettings
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import user_profile.domain.model.UserProfile

class UserProfileCacheDataSourceImpl(
    private val observableSettings: ObservableSettings
) : UserProfileCacheDataSource {

    override suspend fun cacheUserProfile(userProfile: UserProfile) = withContext(Dispatchers.IO) {
        runCatchingLocal {
            observableSettings.putString(
                key = USER_PROFILE,
                value = Json.encodeToString(userProfile)
            )
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile?, DataError.Local> =
        withContext(Dispatchers.IO) {
            runCatchingLocal {
                observableSettings.getStringOrNull(key = USER_PROFILE)?.let {
                    Json.decodeFromString(string = it)
                }
            }

        }

    override suspend fun clearCachedUserProfile() {
        withContext(Dispatchers.IO) {
            runCatchingLocal {
                observableSettings.remove(USER_PROFILE)
            }
        }
    }

    companion object {
        private const val USER_PROFILE = "user_profile"
    }
}