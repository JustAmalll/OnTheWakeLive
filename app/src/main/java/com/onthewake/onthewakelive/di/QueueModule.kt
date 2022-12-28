package com.onthewake.onthewakelive.di

import android.content.Context
import android.content.SharedPreferences
import com.onthewake.onthewakelive.feature_queue.data.remote.*
import com.onthewake.onthewakelive.core.util.Constants
import com.onthewake.onthewakelive.core.util.Constants.PREFS_JWT_TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QueueModule {

    @Provides
    @Singleton
    fun provideHttpClient(prefs: SharedPreferences): HttpClient =
        HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            install(DefaultRequest) {
                val token = prefs.getString(PREFS_JWT_TOKEN, "")
                header("Authorization", "Bearer $token")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

    @Provides
    @Singleton
    fun provideQueueApi(client: OkHttpClient): QueueApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(QueueApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQueueService(
        client: HttpClient, queueApi: QueueApi
    ): QueueService = QueueServiceImpl(client, queueApi)

    @Provides
    @Singleton
    fun provideQueueSocketService(
        client: HttpClient, @ApplicationContext context: Context
    ): QueueSocketService = QueueSocketServiceImpl(context = context, client = client)

}