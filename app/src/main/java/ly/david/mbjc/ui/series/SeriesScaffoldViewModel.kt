package ly.david.mbjc.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.SeriesRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class SeriesScaffoldViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.SERIES
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val series: MutableStateFlow<SeriesListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        seriesId: String,
        selectedTab: SeriesTab
    ) {
        when (selectedTab) {
            SeriesTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val seriesListItemModel = repository.lookupSeries(seriesId)
                        if (title.value.isEmpty()) {
                            title.value = seriesListItemModel.getNameWithDisambiguation()
                        }
                        series.value = seriesListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = seriesId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            SeriesTab.RELATIONSHIPS -> loadRelations(seriesId)
            else -> {
                // Not handled here.
            }
        }
    }
}
