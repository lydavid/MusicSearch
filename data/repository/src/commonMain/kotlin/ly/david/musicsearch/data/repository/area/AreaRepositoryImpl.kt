package ly.david.musicsearch.data.repository.area

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class AreaRepositoryImpl(
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : AreaRepository {

    override suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): AreaDetailsModel {
        if (forceRefresh) {
            delete(areaId)
        }

        val area = areaDao.getAreaForDetails(areaId)
        val urlRelations = relationRepository.getRelationshipsByType(areaId)
        val visited = relationRepository.visited(areaId)
        if (area?.type != null &&
            visited &&
            !forceRefresh
        ) {
            return area.copy(
                urls = urlRelations,
            )
        }

        val areaMusicBrainzModel = lookupApi.lookupArea(areaId)
        cache(
            area = areaMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupArea(
            areaId = areaId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(areaId: String) {
        areaDao.withTransaction {
            areaDao.delete(areaId)
            relationRepository.deleteRelationshipsByType(entityId = areaId)
        }
    }

    private fun cache(
        area: AreaMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        areaDao.withTransaction {
            areaDao.insertReplace(
                area.copy(
                    type = area.type ?: "",
                ),
            )

            val relationWithOrderList = area.relations.toRelationWithOrderList(area.id)
            relationRepository.insertAllUrlRelations(
                entityId = area.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
