package ly.david.musicsearch.shared.domain.browse

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

// TODO: remove localCount and drop from database
data class BrowseEntityCount(
    val browseEntity: MusicBrainzEntity,
    val localCount: Int,
    val remoteCount: Int?,
)
