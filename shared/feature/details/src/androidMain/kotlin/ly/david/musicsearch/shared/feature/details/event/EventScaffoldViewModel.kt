package ly.david.musicsearch.shared.feature.details.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.event.EventRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EventScaffoldViewModel(
    private val repository: EventRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val logger: Logger,
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
        relationsList.entity = entity
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
                        logger.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = eventId,
                                title = title.value,
                                entity = entity,
                            ),
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
