package user_profile.data.source.remote

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import user_profile.domain.model.UpdateUserProfileRequest
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
        updateRequest: UpdateUserProfileRequest,
        photo: ByteArray?
    ): Result<Unit, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.submitFormWithBinaryData(
                url = "/update_profile",
                formData = formData {
                    append(
                        key = "updateRequest",
                        value = Json.encodeToString(updateRequest)
                    )

                    photo?.let {
                        append(
                            key = "photo",
                            value = photo,
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, "image/*")
                                append(HttpHeaders.ContentDisposition, "filename=ktor_logo.png")
                            }
                        )
                    }
                }
            )
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
