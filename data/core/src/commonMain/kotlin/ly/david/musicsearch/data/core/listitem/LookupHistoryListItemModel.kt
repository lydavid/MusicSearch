package ly.david.musicsearch.data.core.listitem

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
) : ListItemModel()
