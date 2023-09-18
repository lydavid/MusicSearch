package ly.david.data.domain.work

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.work.RoomWorkDao
import ly.david.data.room.work.WorkWithAllData
import ly.david.data.room.work.toWorkAttributeRoomModel
import ly.david.data.room.work.toWorkRoomModel
import org.koin.core.annotation.Single

@Single
class WorkRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val workDao: RoomWorkDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupWork(
        workId: String,
    ): WorkScaffoldModel {
        val workWithAllData: WorkWithAllData? = workDao.getWork(workId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(workId)
        if (workWithAllData != null && hasUrlsBeenSavedForEntity) {
            return workWithAllData.toWorkScaffoldModel()
        }

        val workMusicBrainzModel = musicBrainzApi.lookupWork(workId = workId)
        workDao.withTransaction {
            workDao.insert(workMusicBrainzModel.toWorkRoomModel())
            workDao.insertAllAttributes(
                workMusicBrainzModel.attributes?.map { it.toWorkAttributeRoomModel(workId) }.orEmpty()
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
