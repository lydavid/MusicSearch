package ly.david.musicsearch.data.core.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class LookupHistory(
    val mbid: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val searchHint: String = "",
    val deleted: Boolean = false,
)
