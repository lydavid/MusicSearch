package ly.david.mbjc.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.event.EventRepository
import ly.david.data.domain.event.EventScaffoldModel
import ly.david.data.domain.history.IncrementLookupHistoryUseCase
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class EventScaffoldViewModel(
    private val repository: EventRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.EVENT
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val event: MutableStateFlow<EventScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        eventId: String,
        selectedTab: EventTab,
    ) {
        when (selectedTab) {
            EventTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val eventListItemModel = repository.lookupEvent(eventId)
                        if (title.value.isEmpty()) {
                            title.value = eventListItemModel.getNameWithDisambiguation()
                        }
                        event.value = eventListItemModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = eventId,
                                title = title.value,
                                entity = entity,
                            )
                        )
                        recordedLookup = true
                    }
                }
            }

            EventTab.RELATIONSHIPS -> loadRelations(eventId)
            else -> {
                // Not handled here.
            }
        }
    }
}
