package ly.david.musicsearch.shared.domain.error

import kotlin.time.Clock
import kotlin.time.Instant

sealed interface Feedback {
    val message: String

    /**
     * Used to ensure actions with the same message will be shown.
     */
    val time: Instant

    data class Loading(
        override val message: String,
        override val time: Instant = Clock.System.now(),
    ) : Feedback

    data class Success(
        override val message: String,
        override val time: Instant = Clock.System.now(),
    ) : Feedback

    data class Error(
        override val message: String,
        val errorResolution: ErrorResolution,
        val action: Action? = null,
        override val time: Instant = Clock.System.now(),
    ) : Feedback

    data class Actionable(
        override val message: String,
        val action: Action,
        override val time: Instant = Clock.System.now(),
    ) : Feedback
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
