package com.onthewake.onthewakelive.feature_profile.domain.repository

import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_profile.domain.module.UpdateProfileData
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource

interface ProfileRepository {
    suspend fun getProfile(): Resource<Profile>
    suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        profilePictureUri: String
    ): SimpleResource
}