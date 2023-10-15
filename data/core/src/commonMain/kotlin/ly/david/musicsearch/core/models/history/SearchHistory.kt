package ly.david.musicsearch.core.models.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class SearchHistory(
    val query: String,
    val entity: MusicBrainzEntity,
    val lastAccessed: Instant = Clock.System.now(),
)
