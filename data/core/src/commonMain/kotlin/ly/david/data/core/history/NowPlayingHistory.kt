package ly.david.data.core.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class NowPlayingHistory(
    val raw: String,
    val lastPlayed: Instant = Clock.System.now(),
)
