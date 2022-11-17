package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.WorkUiModel
import ly.david.data.domain.toWorkUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi.Companion.WORK_INC_DEFAULT
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.persistence.work.WorkDao
import ly.david.data.persistence.work.toWorkRoomModel

@Singleton
class WorkRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val workDao: WorkDao,
    private val relationDao: RelationDao,
) : RelationsListRepository {

    suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean = false,
        hasRelationsBeenStored: suspend () -> Boolean,
        markResourceHasRelations: suspend () -> Unit
    ): WorkUiModel {
        val workRoomModel = workDao.getWork(workId)
        if (!forceRefresh && workRoomModel != null && hasRelationsBeenStored()) {
            return workRoomModel.toWorkUiModel()
        }

        val workMusicBrainzModel = musicBrainzApiService.lookupWork(
            workId = workId,
            include = WORK_INC_DEFAULT
        )
        val relations = mutableListOf<RelationRoomModel>()
        workMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = workId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)
        markResourceHasRelations()

        workDao.insert(workMusicBrainzModel.toWorkRoomModel())
        return workMusicBrainzModel.toWorkUiModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupWork(
            workId = resourceId,
            include = WORK_INC_DEFAULT
        ).relations
    }
}
