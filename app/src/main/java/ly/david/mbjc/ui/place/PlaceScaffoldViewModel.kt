package ly.david.mbjc.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.PlaceRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class PlaceScaffoldViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.PLACE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val place: MutableStateFlow<PlaceListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        placeId: String,
        selectedTab: PlaceTab
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
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = placeId,
                            resource = resource,
                            summary = title.value
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
