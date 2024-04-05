package ly.david.musicsearch.data.common.network.di

import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.darwin.DarwinClientEngineConfig
import io.ktor.client.plugins.HttpRequestRetry
import ly.david.musicsearch.data.common.network.ApiHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val HttpClientModule: Module = module {
    single {
        ApiHttpClient.configAndCreate(engine = Darwin.create()) {
//            install(HttpRequestRetry) {
//                retryOnExceptionOrServerErrors(maxRetries = 3)
//                exponentialDelay()
//            }
        }
    }
}
