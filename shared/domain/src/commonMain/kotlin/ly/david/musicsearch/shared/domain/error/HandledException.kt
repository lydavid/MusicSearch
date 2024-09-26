package ly.david.musicsearch.shared.domain.error

class HandledException(
    val userMessage: String,
    val errorResolution: ErrorResolution = ErrorResolution.Unknown,
) : Exception(userMessage)
