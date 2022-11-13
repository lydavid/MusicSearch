package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.LabelUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val repository: LabelRepository,
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

    suspend fun lookupLabel(labelId: String): LabelUiModel =
        repository.lookupLabel(labelId)

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupLabel(
            labelId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
