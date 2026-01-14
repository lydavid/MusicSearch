package ly.david.musicsearch.data.repository.place

import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : PlaceRepository {

    override suspend fun lookupPlace(
        placeId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): PlaceDetailsModel = withContext(coroutineDispatchers.io) {
        val cachedData = getCachedData(placeId)
        return@withContext if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val placeMusicBrainzModel = lookupApi.lookupPlace(placeId)
            placeDao.withTransaction {
                if (forceRefresh) {
                    delete(placeId)
                }
                cache(
                    oldId = placeId,
                    place = placeMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(placeMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(placeId: String): PlaceDetailsModel? {
        if (!relationRepository.visited(placeId)) return null
        val place = placeDao.getPlaceForDetails(placeId) ?: return null

        val area = areaDao.getAreaByPlace(placeId)
        val urlRelations = relationRepository.getRelationshipsByType(placeId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.PLACE,
            mbid = placeId,
        )

        return place.copy(
            area = area,
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        placeDao.delete(id)
        areaDao.deleteAreaPlaceLink(id)
        relationRepository.deleteRelationshipsByType(id)
    }

    private fun cache(
        oldId: String,
        place: PlaceMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        placeDao.upsert(
            oldId = oldId,
            place = place,
        )

        aliasDao.insertAll(listOf(place))

        place.area?.let { areaMusicBrainzModel ->
            areaDao.insert(areaMusicBrainzModel)
            placeDao.insertPlaceByArea(
                entityId = areaMusicBrainzModel.id,
                placeId = place.id,
            )
        }

        val relationWithOrderList = place.relations.toRelationWithOrderList(place.id)
        relationRepository.insertRelations(
            entityId = place.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
