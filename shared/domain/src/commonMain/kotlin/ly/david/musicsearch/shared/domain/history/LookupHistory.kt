package ly.david.musicsearch.shared.domain.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
