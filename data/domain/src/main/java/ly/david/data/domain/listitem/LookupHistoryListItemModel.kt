package ly.david.data.domain.listitem

import java.util.Date
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.history.LookupHistoryForListItem

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Date = Date(),
    val imageUrl: String? = null
) : ListItemModel()

fun LookupHistoryForListItem.toLookupHistoryListItemModel() =
    LookupHistoryListItemModel(
        id = lookupHistory.id,
        title = lookupHistory.title,
        entity = lookupHistory.entity,
        numberOfVisits = lookupHistory.numberOfVisits,
        lastAccessed = lookupHistory.lastAccessed,
        imageUrl = thumbnailUrl
    )
