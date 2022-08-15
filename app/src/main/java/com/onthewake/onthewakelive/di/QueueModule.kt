package com.onthewake.onthewakelive.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueService
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueServiceImpl
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueSocketService
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueSocketServiceImpl
import com.onthewake.onthewakelive.util.Constants.PREFS_JWT_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
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
    fun provideQueueService(
        client: HttpClient
    ): QueueService = QueueServiceImpl(client)

    @Provides
    @Singleton
    fun provideQueueSocketService(
        client: HttpClient, @ApplicationContext context: Context
    ): QueueSocketService = QueueSocketServiceImpl(context = context, client = client)

}