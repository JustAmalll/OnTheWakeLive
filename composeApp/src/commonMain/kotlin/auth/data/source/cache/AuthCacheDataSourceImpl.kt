package auth.data.source.cache

import com.russhwolf.settings.ObservableSettings

class AuthCacheDataSourceImpl(
    private val observableSettings: ObservableSettings
) : AuthCacheDataSource {

    override suspend fun cacheJwtTokenAndUserId(token: String, userId: String) {
        observableSettings.putString(key = PREFS_JWT_TOKEN, value = token)
        observableSettings.putString(key = PREFS_USER_ID, value = userId)
    }

    override suspend fun getUserId(): String? =
        observableSettings.getStringOrNull(PREFS_USER_ID)

    override suspend fun logout() {
        observableSettings.clear()
    }

    companion object {
        const val PREFS_JWT_TOKEN = "jwt_token"
        private const val PREFS_USER_ID = "user_id"
    }
}