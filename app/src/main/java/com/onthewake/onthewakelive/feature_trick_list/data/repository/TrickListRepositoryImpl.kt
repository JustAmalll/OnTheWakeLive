package com.onthewake.onthewakelive.feature_trick_list.data.repository

import android.content.Context
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_trick_list.data.local.TrickListDao
import com.onthewake.onthewakelive.feature_trick_list.data.local.toTrickList
import com.onthewake.onthewakelive.feature_trick_list.data.remote.TrickListApi
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickListDto
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.toTrickList
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.toTrickListEntity
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
import com.onthewake.onthewakelive.util.Resource
import com.onthewake.onthewakelive.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TrickListRepositoryImpl(
    private val trickListApi: TrickListApi,
    private val context: Context,
    private val dao: TrickListDao
) : TrickListRepository {

    override suspend fun getTrickList(): Flow<Resource<TrickList>> = flow {

        val trickList = dao.getTrickList()?.toTrickList()
        emit(Resource.Success(data = trickList))

        if (trickList != null) {
            return@flow
        }

        val remoteTrickList = try {
            trickListApi.getTrickList()
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.couldnt_reach_server)))
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(context.getString(R.string.something_went_wrong)))
            null
        }

        remoteTrickList?.let {
            dao.deleteTrickList()
            dao.insertTrickList(it.toTrickListEntity())
            emit(Resource.Success(data = dao.getTrickList()?.toTrickList()))
        }
    }

    override suspend fun getUsersTrickList(userId: String): Resource<TrickList> {
        return try {
            val response = trickListApi.getUsersTrickList(userId)
            Resource.Success(response.toTrickList())
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.couldnt_reach_server))
        } catch (e: HttpException) {
            return if (e.code() == 409) {
                Resource.Error(context.getString(R.string.empty_users_trick_list))
            } else {
                Resource.Error(context.getString(R.string.something_went_wrong))
            }
        }
    }

    override suspend fun addTrickList(trickList: TrickListDto): SimpleResource {
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