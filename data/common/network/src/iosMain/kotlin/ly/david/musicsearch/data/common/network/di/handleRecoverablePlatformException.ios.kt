package ly.david.musicsearch.data.common.network.di

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException

actual fun handleRecoverablePlatformException(exception: Throwable) {
    when (exception) {
        is DarwinHttpRequestException -> {
            throw HandledException("Network error.  Check your internet connection.", ErrorResolution.Retry)
        }
    }
}
