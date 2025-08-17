package ly.david.musicsearch.shared.domain.listitem

import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntityType,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
) : ListItemModel
