package ly.david.musicsearch.shared.domain.place

interface PlaceRepository {
    suspend fun lookupPlace(
        placeId: String,
        forceRefresh: Boolean,
    ): PlaceDetailsModel
}
