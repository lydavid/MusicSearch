package ly.david.mbjc.ui.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class RecordingViewModel @Inject constructor(
    private val repository: RecordingRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val releasesList: ReleasesList,
    private val relationsList: RelationsList,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao
) : ViewModel(), RecordLookupHistory,
    IReleasesList by releasesList,
    IRelationsList by relationsList, RelationsList.Delegate {

    init {
        releasesList.scope = viewModelScope
        releasesList.repository = repository

        relationsList.scope = viewModelScope
        relationsList.delegate = this
    }

    suspend fun lookupRecording(recordingId: String) =
        repository.lookupRecording(recordingId)

    override suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {

        val recordingMusicBrainzModel = musicBrainzApiService.lookupRecording(
            recordingId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        )

        val relations = mutableListOf<RelationRoomModel>()
        recordingMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = resourceId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)

        markResourceHasRelations()
    }
}
