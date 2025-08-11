package ly.david.musicsearch.shared.domain.history

import kotlin.time.Clock
import kotlin.time.Instant

data class NowPlayingHistory(
    val raw: String,
    val lastPlayed: Instant = Clock.System.now(),
)
