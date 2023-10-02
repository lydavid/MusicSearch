package ly.david.data.domain.listitem

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.data.core.history.LookupHistoryForListItem
import ly.david.data.core.network.MusicBrainzEntity

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
) : ListItemModel()

fun LookupHistoryForListItem.toLookupHistoryListItemModel() =
    LookupHistoryListItemModel(
        id = mbid,
        title = title,
        entity = entity,
        numberOfVisits = numberOfVisits,
        lastAccessed = lastAccessed,
        imageUrl = imageUrl,
    )
