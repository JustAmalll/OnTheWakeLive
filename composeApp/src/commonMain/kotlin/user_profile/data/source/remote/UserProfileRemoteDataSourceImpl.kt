package user_profile.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import user_profile.data.source.remote.mapper.toUpdateUserProfileRequest
import user_profile.domain.model.UserProfile

class UserProfileRemoteDataSourceImpl(
    private val client: HttpClient
) : UserProfileRemoteDataSource {

    override suspend fun getUserProfile(): Result<UserProfile, DataError.Network> =
        runCatchingNetwork {
            client.get("/profile").body<UserProfile>()
        }

    override suspend fun updateUserProfile(
        userProfile: UserProfile
    ): Result<Unit, DataError.Network> = runCatchingNetwork {
        client.put("/update_profile") {
            setBody(userProfile.toUpdateUserProfileRequest())
        }
    }
}
