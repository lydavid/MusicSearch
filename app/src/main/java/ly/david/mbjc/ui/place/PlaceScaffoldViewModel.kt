package ly.david.mbjc.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.history.LookupHistory
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.history.IncrementLookupHistory
import ly.david.musicsearch.domain.place.PlaceRepository
import ly.david.musicsearch.domain.place.PlaceScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class PlaceScaffoldViewModel(
    private val repository: PlaceRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistory: IncrementLookupHistory,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.PLACE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val place: MutableStateFlow<PlaceScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        placeId: String,
        selectedTab: PlaceTab,
    ) {
        when (selectedTab) {
            PlaceTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val eventListItemModel = repository.lookupPlace(placeId)
                        if (title.value.isEmpty()) {
                            title.value = eventListItemModel.getNameWithDisambiguation()
                        }
                        place.value = eventListItemModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = placeId,
                                title = title.value,
                                entity = entity,
                            )
                        )
                        recordedLookup = true
                    }
                }
            }

            PlaceTab.RELATIONSHIPS -> loadRelations(placeId)
            else -> {
                // Not handled here.
            }
        }
    }
}
