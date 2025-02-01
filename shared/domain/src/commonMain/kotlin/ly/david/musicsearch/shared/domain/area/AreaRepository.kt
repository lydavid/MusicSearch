package ly.david.musicsearch.shared.domain.area

interface AreaRepository {
    suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
    ): AreaDetailsModel
}
