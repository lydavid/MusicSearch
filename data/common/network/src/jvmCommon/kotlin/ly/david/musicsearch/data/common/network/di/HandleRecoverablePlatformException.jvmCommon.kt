package ly.david.musicsearch.data.common.network.di

import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException

actual fun handleRecoverablePlatformException(exception: Throwable) {
    when (exception) {
        is java.net.ConnectException,
        is java.net.UnknownHostException,
        is java.net.SocketTimeoutException,
        -> {
            throw HandledException("Network error. Check your internet connection.", ErrorResolution.Retry)
        }
    }
}
