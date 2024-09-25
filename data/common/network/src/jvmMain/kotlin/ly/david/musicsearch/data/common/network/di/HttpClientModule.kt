package ly.david.musicsearch.data.common.network.di

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.data.common.network.ApiHttpClient
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import org.koin.core.module.Module
import org.koin.dsl.module

private suspend fun handleRecoverableException(
    exception: Throwable,
) {
    when (exception) {
        is NoTransformationFoundException -> {
            throw RecoverableNetworkException("Requested json but got xml")
        }

        is ClientRequestException -> {
            val exceptionResponse = exception.response
            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                val exceptionResponseText = exceptionResponse.bodyAsText()
                throw RecoverableNetworkException(exceptionResponseText)
            }
        }
    }
}

actual val HttpClientModule: Module = module {
    single {
        ApiHttpClient.configAndCreate(
            engine = OkHttpEngine(config = OkHttpConfig()),
        ) {
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    handleRecoverableException(exception)
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
