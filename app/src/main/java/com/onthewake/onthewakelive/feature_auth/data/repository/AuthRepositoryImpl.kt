package com.onthewake.onthewakelive.feature_auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.onesignal.OneSignal
import com.onthewake.onthewakelive.feature_auth.data.remote.AuthApi
import com.onthewake.onthewakelive.feature_auth.data.remote.request.AuthRequest
import com.onthewake.onthewakelive.feature_auth.data.remote.request.CreateAccountRequest
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.util.Constants
import com.onthewake.onthewakelive.util.Constants.PREFS_FIRST_NAME
import com.onthewake.onthewakelive.util.Constants.PREFS_JWT_TOKEN
import com.onthewake.onthewakelive.util.Constants.PREFS_USER_ID
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences,
    private val context: Context
) : AuthRepository {

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String
    ): AuthResult<Unit> = try {
        api.signUp(
            request = CreateAccountRequest(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                password = password
            )
        )
        signIn(phoneNumber, password)
    } catch (e: HttpException) {
        when (e.code()) {
            401 -> AuthResult.Unauthorized()
            409 -> AuthResult.UserAlreadyExist()
            else -> AuthResult.UnknownError()
        }
    } catch (e: Exception) {
        AuthResult.UnknownError()
    }

    override suspend fun signIn(
        phoneNumber: String, password: String
    ): AuthResult<Unit> = try {

        val response = api.signIn(
            request = AuthRequest(phoneNumber = phoneNumber, password = password)
        )

        prefs.edit().apply {
            putString(PREFS_JWT_TOKEN, response.token).apply()
            putString(PREFS_USER_ID, response.userId).apply()
            putString(PREFS_FIRST_NAME, response.firstName).apply()
        }

        OneSignal.initWithContext(context)
        OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)
        OneSignal.setExternalUserId(response.userId)

        AuthResult.Authorized()
    } catch (e: HttpException) {
        when (e.code()) {
            401 -> AuthResult.Unauthorized()
            409 -> AuthResult.IncorrectData()
            else -> AuthResult.UnknownError()
        }
    } catch (e: Exception) {
        AuthResult.UnknownError()
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString(
                PREFS_JWT_TOKEN, null
            ) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) AuthResult.Unauthorized() else AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}