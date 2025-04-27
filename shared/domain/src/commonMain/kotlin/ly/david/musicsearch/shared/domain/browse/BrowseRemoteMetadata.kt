package ly.david.musicsearch.shared.domain.browse

import kotlinx.datetime.Instant

data class BrowseRemoteMetadata(
    val remoteCount: Int,
    val lastUpdated: Instant,
)
