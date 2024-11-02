package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.history.usecase.DeleteLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.GetPagedHistory
import ly.david.musicsearch.shared.domain.history.usecase.MarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.history.usecase.UnMarkLookupHistoryForDeletion
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class HistoryPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getPagedHistory: GetPagedHistory,
    private val markLookupHistoryForDeletion: MarkLookupHistoryForDeletion,
    private val unMarkLookupHistoryForDeletion: UnMarkLookupHistoryForDeletion,
    private val deleteLookupHistory: DeleteLookupHistory,
) : Presenter<HistoryUiState> {
    @Composable
    override fun present(): HistoryUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val sortOption by appPreferences.historySortOption.collectAsState(HistorySortOption.RECENTLY_VISITED)
        val lazyPagingItems: LazyPagingItems<ListItemModel> = getPagedHistory(
            query = query,
            sortOption = sortOption,
        ).collectAsLazyPagingItems()
        val lazyListState = rememberLazyListState()

        fun eventSink(event: HistoryUiEvent) {
            when (event) {
                is HistoryUiEvent.UpdateSortOption -> {
                    appPreferences.setHistorySortOption(event.sortOption)
                }

                is HistoryUiEvent.MarkHistoryForDeletion -> {
                    markLookupHistoryForDeletion(event.history.id)
                }

                is HistoryUiEvent.UnMarkHistoryForDeletion -> {
                    unMarkLookupHistoryForDeletion(event.history.id)
                }

                is HistoryUiEvent.DeleteHistory -> {
                    deleteLookupHistory(event.history.id)
                }

                HistoryUiEvent.MarkAllHistoryForDeletion -> {
                    markLookupHistoryForDeletion()
                }

                HistoryUiEvent.UnMarkAllHistoryForDeletion -> {
                    unMarkLookupHistoryForDeletion()
                }

                HistoryUiEvent.DeleteAllHistory -> {
                    deleteLookupHistory()
                }

                is HistoryUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            if (event.entity == MusicBrainzEntity.COLLECTION) {
                                CollectionScreen(
                                    id = event.id,
                                )
                            } else {
                                DetailsScreen(
                                    entity = event.entity,
                                    id = event.id,
                                    title = event.title,
                                )
                            },
                        ),
                    )
                }
            }
        }

        return HistoryUiState(
            topAppBarFilterState = topAppBarFilterState,
            sortOption = sortOption,
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class HistoryUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val sortOption: HistorySortOption,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (HistoryUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface HistoryUiEvent : CircuitUiEvent {
    data class UpdateSortOption(val sortOption: HistorySortOption) : HistoryUiEvent
    data class MarkHistoryForDeletion(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data class UnMarkHistoryForDeletion(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data class DeleteHistory(val history: LookupHistoryListItemModel) : HistoryUiEvent
    data object MarkAllHistoryForDeletion : HistoryUiEvent
    data object UnMarkAllHistoryForDeletion : HistoryUiEvent
    data object DeleteAllHistory : HistoryUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : HistoryUiEvent
}
