package ly.david.musicsearch.domain.place

import ly.david.musicsearch.core.models.place.PlaceScaffoldModel

interface PlaceRepository {
    suspend fun lookupPlace(placeId: String): PlaceScaffoldModel
}
