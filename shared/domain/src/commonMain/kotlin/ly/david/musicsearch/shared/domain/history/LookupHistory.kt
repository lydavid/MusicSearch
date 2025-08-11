package ly.david.musicsearch.shared.domain.history

import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class LookupHistory(
    val mbid: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val searchHint: String = "",
    val deleted: Boolean = false,
)
