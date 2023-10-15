package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.label.LabelScaffoldModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.label.LabelRepository
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class LabelScaffoldViewModel(
    private val repository: LabelRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.LABEL
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val label: MutableStateFlow<LabelScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.entity = entity
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
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = labelId,
                                title = title.value,
                                entity = entity,
                            ),
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
