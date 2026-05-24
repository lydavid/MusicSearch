package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import kotlin.time.Instant

interface ArtistRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ArtistDetailsModel
}
