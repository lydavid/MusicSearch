package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import kotlin.time.Clock
import kotlin.time.Instant

data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntityType,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageMetadata: ImageMetadata? = null,
) : ListItemModel
