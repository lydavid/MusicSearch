package ly.david.musicsearch.data.core.browse

import ly.david.musicsearch.data.core.network.MusicBrainzEntity

data class BrowseEntityCount(
    val browseEntity: MusicBrainzEntity,
    val localCount: Int,
    val remoteCount: Int?,
)
