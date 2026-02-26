package ly.david.musicsearch.shared.domain.error

import kotlin.time.Clock
import kotlin.time.Instant

sealed interface Feedback<T> {
    val data: T

    /**
     * Used to ensure actions with the same message will be shown.
     */
    val time: Instant

    data class Loading<T>(
        override val data: T,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    data class Success<T>(
        override val data: T,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    data class Error<T>(
        override val data: T,
        val errorResolution: ErrorResolution,
        val action: Action? = null,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    data class Actionable<T>(
        override val data: T,
        val action: Action,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>
}

// TODO: emit IOState instead of returning this in all places
data class ActionableResult(
    val message: String = "",
    val errorResolution: ErrorResolution = ErrorResolution.None,
    val action: Action? = null,
    val time: Instant = Clock.System.now(),
)

enum class Action {
    Undo,
    Login,
}
