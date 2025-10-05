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
        val cachedData = getCachedData(workId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val workMusicBrainzModel = lookupApi.lookupWork(workId = workId)
            workDao.withTransaction {
                if (forceRefresh) {
                    delete(workId)
                }
                cache(
                    oldId = workId,
                    work = workMusicBrainzModel,
                    lastUpdated = lastUpdated,
                )
            }
            getCachedData(workMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(workId: String): WorkDetailsModel? {
        if (!relationRepository.visited(workId)) return null
        val work = workDao.getWorkForDetails(workId) ?: return null

        val workAttributes = workAttributeDao.getWorkAttributesForWork(workId)
        val urlRelations = relationRepository.getRelationshipsByType(workId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.WORK,
            mbid = workId,
        )

        return work.copy(
            attributes = workAttributes.toPersistentList(),
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        workDao.delete(id)
        relationRepository.deleteRelationshipsByType(id)
    }

    private fun cache(
        oldId: String,
        work: WorkMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        workDao.upsert(
            oldId = oldId,
            work = work,
        )
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
