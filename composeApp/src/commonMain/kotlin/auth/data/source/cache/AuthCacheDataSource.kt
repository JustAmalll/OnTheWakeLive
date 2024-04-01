package auth.data.source.cache

interface AuthCacheDataSource {
    suspend fun cacheJwtTokenAndUserId(token: String, userId: String)
    suspend fun getUserId(): String?
    suspend fun logout()
}