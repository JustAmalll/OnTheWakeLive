package com.onthewake.onthewakelive.feature_trick_list.domain.repository

import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.TrickListResponse
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource

interface TrickListRepository {
    suspend fun getTrickList(): Resource<TrickList>
    suspend fun addTrickList(trickList: TrickListResponse): SimpleResource
}