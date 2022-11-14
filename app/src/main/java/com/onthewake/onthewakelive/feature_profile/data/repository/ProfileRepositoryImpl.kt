package com.onthewake.onthewakelive.feature_profile.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.dataStore
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
                response.message?.let { msg -> Resource.Error(msg) }
                    ?: Resource.Error(context.getString(R.string.unknown_error))
            }
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun updateProfile(
        updateProfileData: UpdateProfileData,
        selectedProfilePictureUri: Uri?
    ): SimpleResource {

        val profilePictureFileName = if (updateProfileData.profilePictureUri.isNotBlank())
            updateProfileData.profilePictureUri.toUri().toFile().name else ""

        return try {
            val uri = uploadToFirebaseStorage(
                storage = storage,
                profilePictureFileName = profilePictureFileName,
                selectedProfilePictureUri = selectedProfilePictureUri
            )

            val response = profileApi.updateProfile(
                updateProfileData = UpdateProfileData(
                    firstName = updateProfileData.firstName,
                    lastName = updateProfileData.lastName,
                    phoneNumber = updateProfileData.phoneNumber,
                    instagram = updateProfileData.instagram,
                    telegram = updateProfileData.telegram,
                    dateOfBirth = updateProfileData.dateOfBirth,
                    profilePictureUri = uri?.toString() ?: ""
                )
            )
            context.dataStore.updateData {
                it.copy(
                    firstName = updateProfileData.firstName.trim(),
                    lastName = updateProfileData.lastName.trim(),
                    phoneNumber = updateProfileData.phoneNumber.trim(),
                    instagram = updateProfileData.instagram.trim(),
                    telegram = updateProfileData.telegram.trim(),
                    dateOfBirth = updateProfileData.dateOfBirth.trim(),
                    profilePictureUri = updateProfileData.profilePictureUri
                )
            }

            if (response.successful) {
                Resource.Success(Unit)
            } else {
                response.message?.let { msg -> Resource.Error(msg) }
                    ?: Resource.Error(context.getString(R.string.unknown_error))
            }
            return Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        }
    }

    private suspend fun uploadToFirebaseStorage(
        storage: FirebaseStorage,
        profilePictureFileName: String,
        selectedProfilePictureUri: Uri?
    ): Uri? {
        return suspendCoroutine { continuation ->

            if (profilePictureFileName.isNotBlank()) {
                val ref = storage.reference.child(profilePictureFileName)

                if (selectedProfilePictureUri != null) {
                    // Upload image to firebase storage and return url to save it on database
                    ref.putFile(selectedProfilePictureUri).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri -> continuation.resume(uri) }
                    }
                } else {
                    // If user didn't select new image, just return old picture url)
                    storage.reference.child(profilePictureFileName).downloadUrl.addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri -> continuation.resume(uri) }
                    }
                }
                // If profile image is blank - return null (when user tries to change
                // profile data without profile image)
            } else continuation.resume(null)
        }
    }
}