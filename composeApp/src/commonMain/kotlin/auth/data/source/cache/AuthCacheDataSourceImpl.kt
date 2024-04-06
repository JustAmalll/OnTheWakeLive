package auth.data.source.cache

import auth.domain.model.AuthResponse
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthCacheDataSourceImpl(
    private val observableSettings: ObservableSettings
) : AuthCacheDataSource {

    override suspend fun cacheAuthResponse(authResponse: AuthResponse) {
        withContext(Dispatchers.IO) {
            observableSettings.putString(key = PREFS_JWT_TOKEN, value = authResponse.token)
            observableSettings.putString(
                key = PREFS_USER_ID,
                value = authResponse.userId.toString()
            )
            observableSettings.putBoolean(key = PREFS_IS_ADMIN, value = authResponse.isAdmin)
        }
    }

    override suspend fun getUserId(): Uuid? = withContext(Dispatchers.IO) {
        observableSettings.getStringOrNull(PREFS_USER_ID)?.let { uuidFrom(it) }
    }

    override suspend fun isUserAdmin(): Boolean = withContext(Dispatchers.IO) {
        observableSettings.getBoolean(key = PREFS_IS_ADMIN, defaultValue = false)
    }


    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            observableSettings.clear()
        }
    }

    companion object {
        const val PREFS_JWT_TOKEN = "jwt_token"
        private const val PREFS_USER_ID = "user_id"
        private const val PREFS_IS_ADMIN = "is_admin"
    }
}