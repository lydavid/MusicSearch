package ly.david.musicsearch.shared.domain.place

import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import kotlin.time.Instant

interface PlaceRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): PlaceDetailsModel
}
