package user_profile.data.source.remote

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import user_profile.domain.model.UserProfile

class UserProfileRemoteDataSourceImpl(
    private val client: HttpClient
) : UserProfileRemoteDataSource {

    override suspend fun getUserProfile(): Result<UserProfile, DataError.Network> =
        withContext(Dispatchers.IO) {
            runCatchingNetwork {
                client.get("/profile").body<UserProfile>()
            }
        }

    override suspend fun updateUserProfile(
        userProfile: UserProfile
    ): Result<Unit, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.put("/update_profile") {
                setBody(userProfile)
            }
            Unit
        }
    }

    override suspend fun getQueueItemDetails(
        userId: Uuid
    ): Result<UserProfile, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.get("/queue_item_details") {
                parameter("userId", userId)
            }.body<UserProfile>()
        }
    }

    override suspend fun searchUsers(
        searchQuery: String
    ): Result<List<UserProfile>, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.get("/search_users") {
                parameter("search_query", searchQuery)
            }.body<List<UserProfile>>()
        }
    }
}
