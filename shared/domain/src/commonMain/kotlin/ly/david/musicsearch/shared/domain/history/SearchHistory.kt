package ly.david.musicsearch.shared.domain.history

import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class SearchHistory(
    val query: String,
    val entity: MusicBrainzEntityType,
    val lastAccessed: Instant = Clock.System.now(),
)
