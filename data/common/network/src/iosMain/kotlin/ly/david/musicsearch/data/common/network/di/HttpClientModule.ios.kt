package ly.david.musicsearch.data.common.network.di

import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import ly.david.musicsearch.data.common.network.ApiHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val HttpClientModule: Module = module {
    single {
        ApiHttpClient.configAndCreate(engine = Darwin.create()) {
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    handleRecoverableException(
                        logger = get(),
                        exception = exception,
                    )
                }
            }
            install(HttpRequestRetry) {
                retryOnExceptionOrServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }
}
