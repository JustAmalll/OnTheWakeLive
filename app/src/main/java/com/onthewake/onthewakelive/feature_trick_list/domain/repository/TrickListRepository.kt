package com.onthewake.onthewakelive.feature_trick_list.domain.repository

import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickListDto
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface TrickListRepository {
    suspend fun getTrickList(): Flow<Resource<TrickList>>
    suspend fun getUsersTrickList(userId: String): Resource<TrickList>
    suspend fun addTrickList(trickList: TrickListDto): SimpleResource
}