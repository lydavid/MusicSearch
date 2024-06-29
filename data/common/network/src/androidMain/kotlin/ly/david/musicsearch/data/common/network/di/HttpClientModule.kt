package ly.david.musicsearch.data.common.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.common.network.ApiHttpClient
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import okhttp3.Cache
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import io.ktor.client.plugins.logging.Logger as KtorLogger

private suspend fun handleRecoverableException(
    logger: Logger,
    exception: Throwable,
) {
    when (exception) {
        is NoTransformationFoundException -> {
            logger.e(exception)
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
        val okHttpConfig = OkHttpConfig()
        okHttpConfig.config {
            cache(
                cache = Cache(
                    directory = File(
                        get<Context>().cacheDir,
                        "ktor_okhttp_cache",
                    ),
                    maxSize = 50 * 1024 * 1024,
                ),
            )
            addInterceptor(ChuckerInterceptor(context = get()))
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

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        get<Logger>().d(message)
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
