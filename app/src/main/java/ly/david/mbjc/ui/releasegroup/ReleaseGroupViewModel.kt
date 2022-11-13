package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.ReleaseGroupRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class ReleaseGroupViewModel @Inject constructor(
    private val repository: ReleaseGroupRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val releasesList: ReleasesList,
    private val relationsList: RelationsList,
    private val musicBrainzApiService: MusicBrainzApiService,
) : ViewModel(), RecordLookupHistory,
    IReleasesList by releasesList,
    IRelationsList by relationsList, RelationsList.Delegate {

    init {
        releasesList.scope = viewModelScope
        releasesList.repository = repository

        relationsList.scope = viewModelScope
        relationsList.delegate = this
    }

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel =
        repository.lookupReleaseGroup(releaseGroupId)

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
