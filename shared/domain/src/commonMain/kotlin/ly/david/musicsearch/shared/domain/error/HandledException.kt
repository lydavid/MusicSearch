package ly.david.musicsearch.shared.domain.error

class HandledException(
    val userMessage: String,
    val errorType: ErrorType? = null,
    val errorResolution: ErrorResolution = ErrorResolution.Unknown,
) : Exception(userMessage)

enum class ErrorType {
    BadRequest,
    Unauthorized,
    Forbidden,
    NotFound,
    InternalServerError,
    LocalNetworkProblem,
}
