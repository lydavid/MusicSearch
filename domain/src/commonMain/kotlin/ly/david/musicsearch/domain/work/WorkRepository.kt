package ly.david.musicsearch.domain.work

import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class WorkRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val workDao: WorkDao,
    private val workAttributeDao: WorkAttributeDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupWork(
        workId: String,
    ): WorkScaffoldModel {
        val work = workDao.getWork(workId)
        val workAttributes = workAttributeDao.getWorkAttributesForWork(workId)
        val urlRelations = relationRepository.getEntityUrlRelationships(workId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(workId)
        if (work != null && hasUrlsBeenSavedForEntity) {
            return work.toWorkScaffoldModel(
                workAttributes = workAttributes,
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
            relationRepository.insertAllUrlRelations(
                entityId = work.id,
                relationMusicBrainzModels = work.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupWork(
            workId = entityId,
            include = INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
