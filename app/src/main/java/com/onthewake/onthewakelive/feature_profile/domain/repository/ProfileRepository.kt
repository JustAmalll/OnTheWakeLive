package com.onthewake.onthewakelive.feature_profile.domain.repository

import android.net.Uri
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_profile.domain.module.UpdateProfileData
import com.onthewake.onthewakelive.core.util.Resource
import com.onthewake.onthewakelive.core.util.SimpleResource

interface ProfileRepository {
    suspend fun getProfile(): Resource<Profile>
    suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        selectedProfilePictureUri: Uri?
    ): SimpleResource
}