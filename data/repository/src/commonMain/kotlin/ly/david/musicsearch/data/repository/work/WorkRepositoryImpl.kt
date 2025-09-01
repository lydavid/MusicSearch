package ly.david.musicsearch.data.repository.work

import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository
import kotlin.time.Instant

class WorkRepositoryImpl(
    private val workDao: WorkDao,
    private val workAttributeDao: WorkAttributeDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : WorkRepository {

    override suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): WorkDetailsModel {
        if (forceRefresh) {
            delete(workId)
        }

        val work = workDao.getWorkForDetails(workId)
        val workAttributes = workAttributeDao.getWorkAttributesForWork(workId)
        val urlRelations = relationRepository.getRelationshipsByType(workId)
        val visited = relationRepository.visited(workId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.WORK,
            mbid = workId,
        )

        if (work != null && visited) {
            return work.copy(
                attributes = workAttributes.toPersistentList(),
                urls = urlRelations,
                aliases = aliases,
            )
        }

        val workMusicBrainzModel = lookupApi.lookupWork(workId = workId)
        cache(
            work = workMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupWork(
            workId = workId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(id: String) {
        workDao.withTransaction {
            workDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        work: WorkMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        workDao.withTransaction {
            workDao.upsert(work)
            workAttributeDao.insertAttributesForWork(
                workId = work.id,
                work.attributes,
            )

            aliasDao.insertAll(listOf(work))

            val relationWithOrderList = work.relations.toRelationWithOrderList(work.id)
            relationRepository.insertAllUrlRelations(
                entityId = work.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
