package com.onthewake.onthewakelive.feature_trick_list.data.repository

import android.content.Context
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_trick_list.data.remote.TrickListApi
import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.TrickListResponse
import com.onthewake.onthewakelive.feature_trick_list.data.remote.response.toTrickList
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource
import retrofit2.HttpException
import java.io.IOException

class TrickListRepositoryImpl(
    private val trickListApi: TrickListApi,
    private val context: Context
) : TrickListRepository {

    override suspend fun getTrickList(): Resource<TrickList> {
        return try {
            val response = trickListApi.getTrickList()
            Resource.Success(response.toTrickList())
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun addTrickList(trickList: TrickListResponse): SimpleResource {
        return try {
            trickListApi.addTrickList(request = trickList)
            Resource.Success(Unit)
        } catch (exception: HttpException) {
            Resource.Error(context.getString(R.string.something_went_wrong))
        } catch (exception: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        }
    }
}