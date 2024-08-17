package ly.david.musicsearch.data.repository.work

import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository

class WorkRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val workDao: WorkDao,
    private val workAttributeDao: WorkAttributeDao,
    private val relationRepository: RelationRepository,
) : WorkRepository {

    override suspend fun lookupWork(
        workId: String,
    ): WorkDetailsModel {
        val work = workDao.getWorkForDetails(workId)
        val workAttributes = workAttributeDao.getWorkAttributesForWork(workId)
        val urlRelations = relationRepository.getEntityUrlRelationships(workId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(workId)
        if (work != null && hasUrlsBeenSavedForEntity) {
            return work.copy(
                attributes = workAttributes,
                urls = urlRelations,
            )
        }

        val workMusicBrainzModel = musicBrainzApi.lookupWork(workId = workId)
        cache(workMusicBrainzModel)
        return lookupWork(workId)
    }

    private fun cache(work: WorkMusicBrainzModel) {
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
