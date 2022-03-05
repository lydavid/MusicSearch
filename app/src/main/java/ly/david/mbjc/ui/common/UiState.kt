package ly.david.mbjc.ui.common

/**
 * Represents a generic UI state.
 */
data class UiState<T>(
    val response: T? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
