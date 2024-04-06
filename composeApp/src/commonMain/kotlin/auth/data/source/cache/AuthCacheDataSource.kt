package auth.data.source.cache

import auth.domain.model.AuthResponse
import com.benasher44.uuid.Uuid

interface AuthCacheDataSource {
    suspend fun cacheAuthResponse(authResponse: AuthResponse)
    suspend fun isUserAdmin(): Boolean
    suspend fun getUserId(): Uuid?
    suspend fun logout()
}