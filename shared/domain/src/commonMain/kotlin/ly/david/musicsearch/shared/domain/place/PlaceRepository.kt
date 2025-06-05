package ly.david.musicsearch.shared.domain.place

import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel

interface PlaceRepository {
    suspend fun lookupPlace(
        placeId: String,
        forceRefresh: Boolean,
    ): PlaceDetailsModel
}
