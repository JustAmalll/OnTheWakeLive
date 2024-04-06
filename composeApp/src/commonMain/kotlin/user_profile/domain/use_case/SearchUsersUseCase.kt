package user_profile.domain.use_case

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile
import user_profile.domain.repository.UserProfileRepository

class SearchUsersUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(searchQuery: String): Result<List<UserProfile>, DataError.Network> =
        userProfileRepository.searchUsers(searchQuery = searchQuery)
}