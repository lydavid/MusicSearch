package ly.david.musicsearch.domain.place

import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class PlaceRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupPlace(placeId: String): PlaceScaffoldModel {
        val place = placeDao.getPlace(placeId)
        val area = areaPlaceDao.getAreaByPlace(placeId)
        val urlRelations = relationRepository.getEntityUrlRelationships(placeId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(placeId)
        if (place != null && hasUrlsBeenSavedForEntity) {
            return place.toPlaceScaffoldModel(area, urlRelations)
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
            relationRepository.insertAllUrlRelations(
                entityId = place.id,
                relationMusicBrainzModels = place.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupPlace(
            placeId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_EVENTS_URLS,
        ).relations
    }
}
