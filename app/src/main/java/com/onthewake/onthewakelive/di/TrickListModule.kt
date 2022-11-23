package com.onthewake.onthewakelive.di

import android.content.Context
import com.onthewake.onthewakelive.feature_trick_list.data.local.TrickListDatabase
import com.onthewake.onthewakelive.feature_trick_list.data.remote.TrickListApi
import com.onthewake.onthewakelive.feature_trick_list.data.repository.TrickListRepositoryImpl
import com.onthewake.onthewakelive.feature_trick_list.domain.repository.TrickListRepository
import com.onthewake.onthewakelive.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrickListModule {

    @Provides
    @Singleton
    fun provideTrickListApi(client: OkHttpClient): TrickListApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TrickListApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTrickListRepository(
        profileApi: TrickListApi,
        db: TrickListDatabase,
        @ApplicationContext context: Context
    ): TrickListRepository = TrickListRepositoryImpl(profileApi, context, db.dao)

}