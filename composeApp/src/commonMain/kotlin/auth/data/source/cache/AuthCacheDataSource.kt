package auth.data.source.cache

import auth.domain.model.AuthResponse

interface AuthCacheDataSource {
    suspend fun cacheAuthResponse(authResponse: AuthResponse)
    suspend fun isUserAdmin(): Boolean
    suspend fun getUserId(): Int?
    suspend fun logout()
}