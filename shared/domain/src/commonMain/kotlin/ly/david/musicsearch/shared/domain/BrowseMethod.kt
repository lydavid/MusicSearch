package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

/**
 * The purpose of this is to distinguish between ways to browse a list of entities.
 * [ByEntity.entity] is NOT the entity we are browsing, it is the entity of the containing screen.
 * (e.g. [MusicBrainzEntity.COLLECTION] while we are browsing works.
 * Or [MusicBrainzEntity.ARTIST] while we are browsing their release groups.)
 */
sealed class BrowseMethod {
    data object All : BrowseMethod()
    data class ByEntity(
        val entityId: String,
        val entity: MusicBrainzEntity,
    ) : BrowseMethod()
}
