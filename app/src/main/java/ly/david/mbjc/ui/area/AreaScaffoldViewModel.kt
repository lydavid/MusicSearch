package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.core.area.AreaScaffoldModel
import ly.david.musicsearch.data.core.area.showReleases
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.history.LookupHistory
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class AreaScaffoldViewModel(
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.AREA
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    var areaTabs: MutableStateFlow<List<AreaTab>> = MutableStateFlow(AreaTab.values().filter { it != AreaTab.RELEASES })
    val area: MutableStateFlow<AreaScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.entity = entity
    }

    fun loadDataForTab(
        areaId: String,
        selectedTab: AreaTab,
    ) {
        when (selectedTab) {
            AreaTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val areaScaffoldModel = repository.lookupArea(areaId)
                        if (title.value.isEmpty()) {
                            title.value = areaScaffoldModel.getNameWithDisambiguation()
                        }
                        if (areaScaffoldModel.showReleases()) {
                            areaTabs.value = AreaTab.values().toList()
                        }
                        area.value = areaScaffoldModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = areaId,
                                title = title.value,
                                entity = entity,
                                searchHint = area.value?.sortName ?: "",
                            )
                        )
                        recordedLookup = true
                    }
                }
            }

            AreaTab.RELATIONSHIPS -> loadRelations(areaId)
            else -> {
                // Not handled here.
            }
        }
    }
}
