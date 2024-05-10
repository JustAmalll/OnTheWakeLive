package auth.data.source.cache

import auth.domain.model.AuthResponse

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
            observableSettings.putInt(key = PREFS_USER_ID, value = authResponse.userId)
            observableSettings.putBoolean(key = PREFS_IS_ADMIN, value = authResponse.isAdmin)
        }
    }

    override suspend fun getUserId(): Int? = withContext(Dispatchers.IO) {
        observableSettings.getIntOrNull(PREFS_USER_ID)
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