package ly.david.musicsearch.core.models.browse

import ly.david.musicsearch.core.models.network.MusicBrainzEntity

data class BrowseEntityCount(
    val browseEntity: MusicBrainzEntity,
    val localCount: Int,
    val remoteCount: Int?,
)
