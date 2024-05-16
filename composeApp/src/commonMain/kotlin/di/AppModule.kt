package di

import auth.data.source.cache.AuthCacheDataSourceImpl.Companion.PREFS_JWT_TOKEN
import com.russhwolf.settings.ObservableSettings
import core.presentation.MainViewModel
import core.presentation.utils.OpenTelegramUtil
import core.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.provideObservableSettings(): ObservableSettings
expect fun Scope.provideOpenTelegramUtil(): OpenTelegramUtil

val appModule = module {
    single { provideObservableSettings() }
    factory { provideOpenTelegramUtil() }
    single { MainViewModel(get(), get(), get(), get(), get()) }

    single {
        HttpClient {
            expectSuccess = true

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(WebSockets)

            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(Constants.BASE_URL)
                val observableSettings by inject<ObservableSettings>()

                observableSettings.getStringOrNull(PREFS_JWT_TOKEN)?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}