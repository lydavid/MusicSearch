package ly.david.musicsearch.data.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiHttpClient {
    fun configAndCreate(
        engine: HttpClientEngine,
        block: HttpClientConfig<*>.() -> Unit,
    ): HttpClient {
        return HttpClient(engine) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

            block()
        }
    }
}
