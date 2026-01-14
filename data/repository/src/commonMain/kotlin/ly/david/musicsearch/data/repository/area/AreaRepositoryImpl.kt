package ly.david.musicsearch.data.repository.area

import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class AreaRepositoryImpl(
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : AreaRepository {

    override suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): AreaDetailsModel = withContext(coroutineDispatchers.io) {
        val cachedData = getCachedData(areaId)
        return@withContext if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val areaMusicBrainzModel = lookupApi.lookupArea(areaId)
            areaDao.withTransaction {
                if (forceRefresh) {
                    delete(areaId)
                }
                cache(
                    oldId = areaId,
                    area = areaMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(areaMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(areaId: String): AreaDetailsModel? {
        if (!relationRepository.visited(areaId)) return null

        val area = areaDao.getAreaForDetails(areaId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(areaId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.AREA,
            mbid = areaId,
        )

        return area.copy(
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(areaId: String) {
        areaDao.delete(areaId)
        relationRepository.deleteRelationshipsByType(entityId = areaId)
    }

    private fun cache(
        oldId: String,
        area: AreaMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        areaDao.upsert(
            oldAreaId = oldId,
            area = area,
        )

        aliasDao.insertAll(listOf(area))

        val relationWithOrderList = area.relations.toRelationWithOrderList(area.id)
        relationRepository.insertRelations(
            entityId = area.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )
    }
}
