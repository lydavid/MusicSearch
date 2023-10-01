package ly.david.mbjc.ui.instrument

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.history.IncrementLookupHistoryUseCase
import ly.david.data.domain.instrument.InstrumentRepository
import ly.david.data.domain.instrument.InstrumentScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class InstrumentScaffoldViewModel(
    private val repository: InstrumentRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.INSTRUMENT
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val instrument: MutableStateFlow<InstrumentScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        instrumentId: String,
        selectedTab: InstrumentTab,
    ) {
        when (selectedTab) {
            InstrumentTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val eventListItemModel = repository.lookupInstrument(instrumentId)
                        if (title.value.isEmpty()) {
                            title.value = eventListItemModel.getNameWithDisambiguation()
                        }
                        instrument.value = eventListItemModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = instrumentId,
                                title = title.value,
                                entity = entity,
                            )
                        )
                        recordedLookup = true
                    }
                }
            }

            InstrumentTab.RELATIONSHIPS -> loadRelations(instrumentId)
            else -> {
                // Not handled here.
            }
        }
    }
}
