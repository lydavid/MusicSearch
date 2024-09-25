package ly.david.musicsearch.shared.domain.error

data class ActionableResult(
    val message: String = "",
    val errorResolution: ErrorResolution = ErrorResolution.None,
    val actionLabel: String? = null,
)
