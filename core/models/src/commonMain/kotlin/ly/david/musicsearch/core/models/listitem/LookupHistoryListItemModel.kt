package ly.david.musicsearch.core.models.listitem

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@Immutable
data class LookupHistoryListItemModel(
    override val id: String,
    val title: String = "",
    val entity: MusicBrainzEntity,
    val numberOfVisits: Int = 1,
    val lastAccessed: Instant = Clock.System.now(),
    val imageUrl: String? = null,
) : ListItemModel()
