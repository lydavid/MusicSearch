package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.label.LabelRepository
import ly.david.data.domain.label.LabelScaffoldModel
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class LabelScaffoldViewModel(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.LABEL
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val label: MutableStateFlow<LabelScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        labelId: String,
        selectedTab: LabelTab,
    ) {
        when (selectedTab) {
            LabelTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val labelListItemModel = repository.lookupLabel(labelId)
                        if (title.value.isEmpty()) {
                            title.value = labelListItemModel.getNameWithDisambiguation()
                        }
                        label.value = labelListItemModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = labelId,
                            entity = entity,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            LabelTab.RELATIONSHIPS -> loadRelations(labelId)
            else -> {
                // Not handled here.
            }
        }
    }
}
