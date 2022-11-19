package com.onthewake.onthewakelive.feature_trick_list.data.remote

import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.TrickListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TrickListApi {

    @GET("/trick_list")
    suspend fun getTrickList(): TrickListResponse

    @POST("/add/trick_list")
    suspend fun addTrickList(
        @Body request: TrickListResponse
    )

}