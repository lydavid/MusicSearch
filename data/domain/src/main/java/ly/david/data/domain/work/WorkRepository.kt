package ly.david.data.domain.work

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
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
        workDao.withTransaction {
            workDao.insert(workMusicBrainzModel)
            workAttributeDao.insertAttributesForWork(
                workId = workId,
                workMusicBrainzModel.attributes.orEmpty()
            )
            relationRepository.insertAllUrlRelations(
                entityId = workId,
                relationMusicBrainzModels = workMusicBrainzModel.relations,
            )
        }
        return lookupWork(workId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupWork(
            workId = entityId,
            include = INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
