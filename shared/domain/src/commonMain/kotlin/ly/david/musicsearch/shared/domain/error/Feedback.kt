package ly.david.musicsearch.shared.domain.error

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize
import kotlin.time.Clock
import kotlin.time.Instant

@Parcelize
sealed interface Feedback<T : CommonParcelable> : CommonParcelable {
    val data: T

    /**
     * Used to ensure feedback with the same [data] can be shown again in Compose.
     */
    val time: Instant

    data class Loading<T : CommonParcelable>(
        override val data: T,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    data class Success<T : CommonParcelable>(
        override val data: T,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    data class Error<T : CommonParcelable>(
        override val data: T,
        val errorResolution: ErrorResolution,
        val action: Action? = null,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>

    /**
     * For intermediate states before a [Success] or [Error], such as soft deletion with an undo action.
     */
    data class Actionable<T : CommonParcelable>(
        override val data: T,
        val action: Action,
        override val time: Instant = Clock.System.now(),
    ) : Feedback<T>
}

/**
 * Change the set time, so that the presenter layer can control the time instead of the repository layer.
 */
fun <T : CommonParcelable> Feedback<T>.withTime(time: Instant): Feedback<T> = when (this) {
    is Feedback.Loading -> copy(time = time)
    is Feedback.Success -> copy(time = time)
    is Feedback.Error -> copy(time = time)
    is Feedback.Actionable -> copy(time = time)
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
