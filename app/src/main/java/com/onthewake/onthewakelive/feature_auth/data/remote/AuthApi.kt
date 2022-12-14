package com.onthewake.onthewakelive.feature_auth.data.remote

import com.onthewake.onthewakelive.feature_auth.data.remote.request.AuthRequest
import com.onthewake.onthewakelive.feature_auth.data.remote.request.CreateAccountRequest
import com.onthewake.onthewakelive.feature_auth.data.remote.response.AuthResponse
import retrofit2.http.*

interface AuthApi {

    @POST("/signup")
    suspend fun signUp(
        @Body request: CreateAccountRequest
    )

    @POST("/signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): AuthResponse

    @GET("/authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @GET("/isUserAlreadyExists")
    suspend fun isUserAlreadyExists(
        @Query("phoneNumber") phoneNumber: String
    ): Boolean
}