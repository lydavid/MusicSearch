package ly.david.musicsearch.data.common.network.di

import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.HandledException

actual fun handleRecoverablePlatformException(exception: Throwable) {
    when (exception) {
        is java.net.ConnectException,
        is java.net.UnknownHostException,
        is java.net.SocketTimeoutException,
        is java.net.SocketException,
        is okhttp3.internal.http2.StreamResetException,
        -> {
            throw HandledException(
                userMessage = "Network error. Check your internet connection.",
                errorType = ErrorType.LocalNetworkProblem,
                errorResolution = ErrorResolution.Retry,
            )
        }
    }
}
