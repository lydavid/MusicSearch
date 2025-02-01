package ly.david.musicsearch.data.repository.place

import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : PlaceRepository {

    override suspend fun lookupPlace(
        placeId: String,
        forceRefresh: Boolean,
    ): PlaceDetailsModel {
        if (forceRefresh) {
            delete(placeId)
        }

        val place = placeDao.getPlaceForDetails(placeId)
        val area = areaPlaceDao.getAreaByPlace(placeId)
        val urlRelations = relationRepository.getRelationshipsByType(placeId)
        val visited = relationRepository.visited(placeId)
        if (place != null &&
            visited &&
            !forceRefresh
        ) {
            return place.copy(
                area = area,
                urls = urlRelations,
            )
        }

        val placeMusicBrainzModel = lookupApi.lookupPlace(placeId)
        cache(placeMusicBrainzModel)
        return lookupPlace(placeId, false)
    }

    private fun delete(id: String) {
        placeDao.withTransaction {
            placeDao.delete(id)
            areaPlaceDao.deleteAreaPlaceLink(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(place: PlaceMusicBrainzModel) {
        placeDao.withTransaction {
            placeDao.insert(place)
            place.area?.let { areaMusicBrainzModel ->
                areaDao.insert(areaMusicBrainzModel)
                areaPlaceDao.insert(
                    areaId = areaMusicBrainzModel.id,
                    placeId = place.id,
                )
            }

            val relationWithOrderList = place.relations.toRelationWithOrderList(place.id)
            relationRepository.insertAllUrlRelations(
                entityId = place.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
