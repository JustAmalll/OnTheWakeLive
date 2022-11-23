package com.onthewake.onthewakelive.feature_trick_list.data.remote

import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TrickListApi {

    @GET("/trick_list")
    suspend fun getTrickList(): TrickListDto

    @GET("/users/trick_list")
    suspend fun getUsersTrickList(
        @Query("userId") userId: String
    ): TrickListDto

    @POST("/add/trick_list")
    suspend fun addTrickList(
        @Body request: TrickListDto
    )

}