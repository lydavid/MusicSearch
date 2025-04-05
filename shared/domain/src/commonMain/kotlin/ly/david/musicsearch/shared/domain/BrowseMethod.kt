package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

sealed class BrowseMethod {
    data object All : BrowseMethod()
    data class ByEntity(
        val entityId: String,
        val entity: MusicBrainzEntity,
    ) : BrowseMethod()
}
