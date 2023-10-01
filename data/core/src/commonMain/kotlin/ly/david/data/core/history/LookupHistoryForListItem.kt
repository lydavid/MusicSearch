package ly.david.data.core.history

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.data.core.network.MusicBrainzEntity

data class LookupHistoryForListItem(
    val mbid: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
)
