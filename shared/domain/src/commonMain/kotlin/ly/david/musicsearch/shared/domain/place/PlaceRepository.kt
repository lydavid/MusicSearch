package ly.david.musicsearch.shared.domain.place

import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel

interface PlaceRepository {
    suspend fun lookupPlace(
        placeId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): PlaceDetailsModel
}
