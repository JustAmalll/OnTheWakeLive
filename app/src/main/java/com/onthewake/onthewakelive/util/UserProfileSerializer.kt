package com.onthewake.onthewakelive.util

import androidx.datastore.core.Serializer
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserProfileSerializer : Serializer<Profile> {

    override val defaultValue: Profile
        get() = Profile(
            userId = "",
            firstName = "",
            lastName = "",
            profilePictureUri = "",
            phoneNumber = "",
            instagram = "",
            telegram = "",
            dateOfBirth = ""
        )

    override suspend fun readFrom(
        input: InputStream
    ): Profile = try {
        Json.decodeFromString(
            deserializer = Profile.serializer(),
            string = input.readBytes().decodeToString()
        )
    } catch (e: SerializationException) {
        e.printStackTrace()
        defaultValue
    }

    override suspend fun writeTo(t: Profile, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = Profile.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}