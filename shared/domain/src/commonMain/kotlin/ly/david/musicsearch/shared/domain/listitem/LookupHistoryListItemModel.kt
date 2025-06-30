package ly.david.musicsearch.shared.domain.listitem

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
) : ListItemModel
