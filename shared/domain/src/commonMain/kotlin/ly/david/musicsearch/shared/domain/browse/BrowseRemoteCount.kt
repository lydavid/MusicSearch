package ly.david.musicsearch.shared.domain.browse

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

/**
 * Tracks the count of [browseEntity] in MusicBrainz.
 */
data class BrowseRemoteCount(
    val browseEntity: MusicBrainzEntity,
    val remoteCount: Int?,
)
