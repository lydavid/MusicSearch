package ly.david.musicsearch.data.repository.area

import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class AreaRepositoryImpl(
    private val areaDao: AreaDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : AreaRepository {

    override suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
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
        cache(areaMusicBrainzModel)
        return lookupArea(
            areaId = areaId,
            forceRefresh = false,
        )
    }

    private fun delete(areaId: String) {
        areaDao.withTransaction {
            areaDao.delete(areaId)
            relationRepository.deleteRelationshipsByType(entityId = areaId)
        }
    }

    private fun cache(area: AreaMusicBrainzNetworkModel) {
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
            )
        }
    }
}
