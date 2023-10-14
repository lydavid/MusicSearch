package ly.david.data.common.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Cache

object ApiHttpClient {
    fun configAndCreate(
        cache: Cache,
        block: HttpClientConfig<*>.() -> Unit,
    ): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true

            engine {
                config {
                    cache(cache)
                }
            }

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
