package di

import core.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(WebSockets)

            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(Constants.BASE_URL)
                header(HttpHeaders.Authorization, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ1c2VycyIsImlzcyI6Imh0dHBzOi8vb250aGV3YWtlbGl2ZS5oZXJva3VhcHAuY29tOjgwODAiLCJleHAiOjE3NDMzNTYzOTIsInVzZXJJZCI6IjY2MDZiODhjZTc0YzNhNjQ2YjM0YzdkYSJ9.RPvbpDoyuitZYalTU1CtazujIGgqtKpVMMC9zYcuMK8")
            }
        }
    }
}