package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

/**
 * The purpose of this is to distinguish between ways to browse a list of entities.
 * [ByEntity.entity] is NOT the entity we are browsing, it is the entity of the containing screen.
 * (e.g. [MusicBrainzEntityType.COLLECTION] while we are browsing works.
 * Or [MusicBrainzEntityType.ARTIST] while we are browsing their release groups.)
 */
@Parcelize
sealed class BrowseMethod : CommonParcelable {
    data object All : BrowseMethod()
    data class ByEntity(
        val entityId: String,
        val entity: MusicBrainzEntityType,
    ) : BrowseMethod()
}
