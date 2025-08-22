package ly.david.musicsearch.data.common.network.di

import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import ly.david.musicsearch.data.common.network.ApiHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val HttpClientModule: Module = module {
    single {
        val okHttpConfig = OkHttpConfig()
        okHttpConfig.config {
            connectTimeout(15, TimeUnit.SECONDS)
        }
        ApiHttpClient.configAndCreate(
            engine = OkHttpEngine(config = okHttpConfig),
        ) {
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    handleRecoverableException(
                        logger = get(),
                        exception = exception,
                    )
                }
            }

            install(HttpCache)

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            install(HttpRequestRetry) {
                retryOnExceptionOrServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }
}
