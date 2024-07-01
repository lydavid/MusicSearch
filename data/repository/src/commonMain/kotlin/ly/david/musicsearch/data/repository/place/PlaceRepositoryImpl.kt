package ly.david.musicsearch.data.repository.place

import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.place.PlaceScaffoldModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class PlaceRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
) : PlaceRepository {

    override suspend fun lookupPlace(placeId: String): PlaceScaffoldModel {
        val place = placeDao.getPlaceForDetails(placeId)
        val area = areaPlaceDao.getAreaByPlace(placeId)
        val urlRelations = relationRepository.getEntityUrlRelationships(placeId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(placeId)
        if (place != null && hasUrlsBeenSavedForEntity) {
            return place.copy(
                area = area,
                urls = urlRelations,
            )
        }

        val placeMusicBrainzModel = musicBrainzApi.lookupPlace(placeId)
        cache(placeMusicBrainzModel)
        return lookupPlace(placeId)
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
