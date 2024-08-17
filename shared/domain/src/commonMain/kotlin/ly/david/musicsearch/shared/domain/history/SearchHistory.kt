package ly.david.musicsearch.shared.domain.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class SearchHistory(
    val query: String,
    val entity: MusicBrainzEntity,
    val lastAccessed: Instant = Clock.System.now(),
)
