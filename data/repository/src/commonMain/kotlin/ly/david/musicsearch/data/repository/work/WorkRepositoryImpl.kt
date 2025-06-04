package ly.david.musicsearch.data.repository.work

import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository

class WorkRepositoryImpl(
    private val workDao: WorkDao,
    private val workAttributeDao: WorkAttributeDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : WorkRepository {

    override suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean,
    ): WorkDetailsModel {
        if (forceRefresh) {
            delete(workId)
        }

        val work = workDao.getWorkForDetails(workId)
        val workAttributes = workAttributeDao.getWorkAttributesForWork(workId)
        val urlRelations = relationRepository.getRelationshipsByType(workId)
        val visited = relationRepository.visited(workId)
        if (work != null && visited) {
            return work.copy(
                attributes = workAttributes,
                urls = urlRelations,
            )
        }

        val workMusicBrainzModel = lookupApi.lookupWork(workId = workId)
        cache(workMusicBrainzModel)
        return lookupWork(workId, false)
    }

    private fun delete(id: String) {
        workDao.withTransaction {
            workDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(work: WorkMusicBrainzNetworkModel) {
        workDao.withTransaction {
            workDao.insert(work)
            workAttributeDao.insertAttributesForWork(
                workId = work.id,
                work.attributes,
            )

            val relationWithOrderList = work.relations.toRelationWithOrderList(work.id)
            relationRepository.insertAllUrlRelations(
                entityId = work.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
