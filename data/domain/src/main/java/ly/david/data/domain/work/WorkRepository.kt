package ly.david.data.domain.work

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.work.WorkDao
import ly.david.data.room.work.toWorkAttributeRoomModel
import ly.david.data.room.work.toWorkRoomModel

@Singleton
class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val workDao: WorkDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupWork(
        workId: String,
    ): WorkScaffoldModel {
        val workWithAllData = workDao.getWork(workId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(workId)
        if (workWithAllData != null && hasUrlsBeenSavedForEntity) {
            return workWithAllData.toWorkScaffoldModel()
        }

        val workMusicBrainzModel = musicBrainzApiService.lookupWork(workId = workId)
        workDao.withTransaction {
            workDao.insert(workMusicBrainzModel.toWorkRoomModel())
            workDao.insertAllAttributes(
                workMusicBrainzModel.attributes?.map { it.toWorkAttributeRoomModel(workId) }.orEmpty()
            )
            relationRepository.insertAllRelations(
                entityId = workId,
                relationMusicBrainzModels = workMusicBrainzModel.relations,
            )
        }
        return lookupWork(workId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupWork(
            workId = entityId,
            include = INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
