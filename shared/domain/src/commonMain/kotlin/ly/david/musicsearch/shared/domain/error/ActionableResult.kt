package ly.david.musicsearch.shared.domain.error

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class ActionableResult(
    val message: String = "",
    val errorResolution: ErrorResolution = ErrorResolution.None,
    val action: Action? = null,

    // Used to ensure actions with the same message will be shown
    val time: Instant = Clock.System.now(),
)

enum class Action {
    Undo,
    Login,
}
