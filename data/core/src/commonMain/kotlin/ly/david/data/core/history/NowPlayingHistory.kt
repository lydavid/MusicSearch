package ly.david.data.core.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.data.core.Identifiable

data class NowPlayingHistory(
    val raw: String,
    val lastPlayed: Instant = Clock.System.now(),
) : Identifiable {
    override val id: String
        get() = raw
}
