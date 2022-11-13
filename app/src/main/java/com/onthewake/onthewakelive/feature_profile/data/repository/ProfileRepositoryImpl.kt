package com.onthewake.onthewakelive.feature_profile.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_profile.data.remote.ProfileApi
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_profile.domain.module.UpdateProfileData
import com.onthewake.onthewakelive.feature_profile.domain.repository.ProfileRepository
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val storage: FirebaseStorage,
    private val context: Context
) : ProfileRepository {

    override suspend fun getProfile(): Resource<Profile> {
        return try {
            val response = profileApi.getProfile()
            if (response.successful) {
                Resource.Success(response.data?.toProfile())
            } else {
                response.message?.let { msg ->
                    Resource.Error(msg)
                } ?: Resource.Error(context.getString(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        profilePictureUri: String
    ): SimpleResource {

        return try {
            val uri = uploadToFirebaseStorage(
                storage,
                updateProfileData.profilePictureFileName,
                profilePictureUri
            )

            val response = profileApi.updateProfile(
                updateProfileData = UpdateProfileData(
                    firstName = updateProfileData.firstName,
                    lastName = updateProfileData.lastName,
                    phoneNumber = updateProfileData.phoneNumber,
                    instagram = updateProfileData.instagram,
                    telegram = updateProfileData.telegram,
                    dateOfBirth = updateProfileData.dateOfBirth,
                    profilePictureFileName = uri.toString()
                )
            )
            if (response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg ->
                    Resource.Error(msg)
                } ?: Resource.Error(context.getString(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        }
    }

    private suspend fun uploadToFirebaseStorage(
        storage: FirebaseStorage,
        profilePictureFileName: String,
        profilePictureUri: String
    ): Uri {
        return suspendCoroutine { continuation ->
            val ref = storage.reference.child(profilePictureFileName)

            ref.putFile(profilePictureUri.toUri()).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri -> continuation.resume(uri) }
            }
        }
    }
}