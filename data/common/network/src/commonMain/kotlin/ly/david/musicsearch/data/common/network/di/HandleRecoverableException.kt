package ly.david.musicsearch.data.common.network.di

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException

@Suppress("ThrowsCount")
internal suspend fun handleRecoverableException(
    logger: Logger,
    exception: Throwable,
) {
    when (exception) {
        is NoTransformationFoundException -> {
            logger.e(exception)
            throw HandledException("Requested json but got xml.", ErrorResolution.Retry)
        }

        // https://ktor.io/docs/client-response-validation.html#non-2xx
        is ClientRequestException -> {
            val exceptionResponse = exception.response
            val handledException: HandledException
            when (exceptionResponse.status) {
                HttpStatusCode.BadRequest -> {
                    handledException = HandledException("Bad request.", ErrorResolution.None)
                }

                HttpStatusCode.Unauthorized -> {
                    handledException = HandledException("You are not logged in.", ErrorResolution.Login)
                }

                HttpStatusCode.Forbidden -> {
                    handledException =
                        HandledException("You do not have permission to access this item.", ErrorResolution.None)
                }

                HttpStatusCode.NotFound -> {
                    handledException = HandledException("This item does not exist.", ErrorResolution.None)
                }

                else -> {
                    logger.e(exception)
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    handledException = HandledException(exceptionResponseText)
                }
            }
            throw handledException
        }

        is ServerResponseException -> {
            val exceptionResponse = exception.response
            val handledException: HandledException
            when (exceptionResponse.status) {
                HttpStatusCode.InternalServerError -> {
                    handledException = HandledException(
                        "${exception.response.request.url.host} may be experiencing issues. Try again later.",
                        ErrorResolution.Retry,
                    )
                }

                else -> {
                    logger.e(exception)
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    handledException = HandledException(exceptionResponseText)
                }
            }
            throw handledException
        }
    }
    handleRecoverablePlatformException(exception)
}
