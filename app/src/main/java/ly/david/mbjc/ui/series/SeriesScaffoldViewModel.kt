package ly.david.mbjc.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.history.LookupHistory
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.history.IncrementLookupHistory
import ly.david.musicsearch.domain.series.SeriesRepository
import ly.david.musicsearch.domain.series.SeriesScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class SeriesScaffoldViewModel(
    private val repository: SeriesRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistory: IncrementLookupHistory,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.SERIES
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val series: MutableStateFlow<SeriesScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        seriesId: String,
        selectedTab: SeriesTab,
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
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = seriesId,
                                title = title.value,
                                entity = entity,
                            )
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
