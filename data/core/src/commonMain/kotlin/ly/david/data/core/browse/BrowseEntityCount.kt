package ly.david.data.core.browse

import ly.david.data.core.network.MusicBrainzEntity

data class BrowseEntityCount(
    val browseEntity: MusicBrainzEntity,
    val localCount: Int,
    val remoteCount: Int?,
)
