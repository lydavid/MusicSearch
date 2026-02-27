package ly.david.musicsearch.data.common.network.di

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.SendCountExceedException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.HandledException

@Suppress("ThrowsCount", "LongMethod")
internal suspend fun handleRecoverableException(
    logger: Logger,
    exception: Throwable,
) {
    when (exception) {
        is NoTransformationFoundException -> {
            logger.e(exception)
            throw HandledException(
                userMessage = "Requested json but got xml.",
                errorResolution = ErrorResolution.Retry,
            )
        }

        // https://ktor.io/docs/client-response-validation.html#non-2xx
        is ClientRequestException -> {
            val exceptionResponse = exception.response
            val handledException: HandledException
            when (exceptionResponse.status) {
                HttpStatusCode.BadRequest -> {
                    handledException = HandledException(
                        userMessage = "Bad request.",
                        errorType = ErrorType.BadRequest,
                        errorResolution = ErrorResolution.None,
                    )
                }

                HttpStatusCode.Unauthorized -> {
                    handledException = HandledException(
                        userMessage = "You are not logged in.",
                        errorType = ErrorType.Unauthorized,
                        errorResolution = ErrorResolution.Login,
                    )
                }

                HttpStatusCode.Forbidden -> {
                    handledException =
                        HandledException(
                            userMessage = "You do not have permission to access this item.",
                            errorType = ErrorType.Forbidden,
                            errorResolution = ErrorResolution.None,
                        )
                }

                HttpStatusCode.NotFound -> {
                    handledException = HandledException(
                        userMessage = "This item does not exist.",
                        errorType = ErrorType.NotFound,
                        errorResolution = ErrorResolution.None,
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

        is ServerResponseException -> {
            val exceptionResponse = exception.response
            val handledException: HandledException
            when (exceptionResponse.status) {
                HttpStatusCode.InternalServerError -> {
                    handledException = HandledException(
                        userMessage = "${exception.response.request.url.host} " +
                            "may be experiencing issues. Try again later.",
                        errorType = ErrorType.InternalServerError,
                        errorResolution = ErrorResolution.Retry,
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

        is SendCountExceedException -> {
            throw HandledException(
                userMessage = "Request timed out.",
                errorResolution = ErrorResolution.Retry,
            )
        }
    }
    handleRecoverablePlatformException(exception)
}
