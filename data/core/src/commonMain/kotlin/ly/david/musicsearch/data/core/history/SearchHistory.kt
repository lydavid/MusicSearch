package ly.david.musicsearch.data.core.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class SearchHistory(
    val query: String,
    val entity: MusicBrainzEntity,
    val lastAccessed: Instant = Clock.System.now(),
)
