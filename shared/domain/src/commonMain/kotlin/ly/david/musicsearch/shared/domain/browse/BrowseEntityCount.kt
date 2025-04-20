package ly.david.musicsearch.shared.domain.browse

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

// TODO: remove localCount and drop from database
//  still used for the number of collections for a user
//  replace by counting the number of remote collections
data class BrowseEntityCount(
    val browseEntity: MusicBrainzEntity,
    val localCount: Int,
    val remoteCount: Int?,
)
