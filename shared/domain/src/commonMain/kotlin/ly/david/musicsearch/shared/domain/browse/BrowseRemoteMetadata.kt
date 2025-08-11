package ly.david.musicsearch.shared.domain.browse

import kotlin.time.Instant

data class BrowseRemoteMetadata(
    val remoteCount: Int,
    val lastUpdated: Instant,
)
