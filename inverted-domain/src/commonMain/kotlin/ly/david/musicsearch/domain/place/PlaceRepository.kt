package ly.david.musicsearch.domain.place

import ly.david.musicsearch.data.core.place.PlaceScaffoldModel

interface PlaceRepository {
    suspend fun lookupPlace(placeId: String): PlaceScaffoldModel
}
