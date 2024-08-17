package ly.david.musicsearch.shared.domain.place

import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel

interface PlaceRepository {
    suspend fun lookupPlace(placeId: String): PlaceDetailsModel
}
