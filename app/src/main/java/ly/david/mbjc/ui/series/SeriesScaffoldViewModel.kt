package ly.david.mbjc.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.data.domain.series.SeriesRepository
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class SeriesScaffoldViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.SERIES
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val series: MutableStateFlow<SeriesListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
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
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = seriesId,
                            entity = entity,
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
