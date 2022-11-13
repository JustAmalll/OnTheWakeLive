package com.onthewake.onthewakelive.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.onthewake.onthewakelive.feature_auth.data.remote.AuthApi
import com.onthewake.onthewakelive.feature_auth.data.repository.AuthRepositoryImpl
import com.onthewake.onthewakelive.feature_auth.domain.repository.AuthRepository
import com.onthewake.onthewakelive.feature_auth.domain.use_cases.ValidationUseCase
import com.onthewake.onthewakelive.util.Constants.BASE_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AuthApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi, prefs: SharedPreferences, @ApplicationContext context: Context
    ): AuthRepository = AuthRepositoryImpl(api, prefs, FirebaseAuth.getInstance(), context)

    @Provides
    @Singleton
    fun provideValidateUseCase(
        @ApplicationContext context: Context
    ): ValidationUseCase = ValidationUseCase(context)

}