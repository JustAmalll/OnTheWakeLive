package com.onthewake.onthewakelive.feature_trick_list.domain.repository

import com.onthewake.onthewakelive.core.util.Resource
import com.onthewake.onthewakelive.core.util.SimpleResource
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import kotlinx.coroutines.flow.Flow

interface TrickListRepository {
    suspend fun getTrickList(): Flow<Resource<TrickList>>
    suspend fun getUsersTrickList(userId: String): Resource<TrickList>
    suspend fun addTrickList(trickList: TrickList): SimpleResource
}