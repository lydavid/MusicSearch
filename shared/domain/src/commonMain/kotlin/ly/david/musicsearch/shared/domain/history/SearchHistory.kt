package ly.david.musicsearch.shared.domain.history

import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class SearchHistory(
    val query: String,
    val entity: MusicBrainzEntity,
    val lastAccessed: Instant = Clock.System.now(),
)
