package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.history.usecase.DeleteLookupHistory
import ly.david.musicsearch.domain.history.usecase.GetPagedHistory
import ly.david.musicsearch.domain.history.usecase.MarkLookupHistoryForDeletion
import ly.david.musicsearch.domain.history.usecase.UnMarkLookupHistoryForDeletion
import ly.david.ui.common.screen.DetailsScreen

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
        var query by rememberSaveable { mutableStateOf("") }
        val sortOption by appPreferences.historySortOption.collectAsState(HistorySortOption.RECENTLY_VISITED)
        val lazyPagingItems: LazyPagingItems<ListItemModel> = getPagedHistory(
            query = query,
            sortOption = sortOption,
        ).collectAsLazyPagingItems()

        fun eventSink(event: HistoryUiEvent) {
            when (event) {
                is HistoryUiEvent.UpdateQuery -> {
                    query = event.query
                }

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
                            DetailsScreen(
                                event.entity,
                                event.id,
                                event.title,
                            ),
                        ),
                    )
                }
            }
        }

        return HistoryUiState(
            query = query,
            sortOption = sortOption,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}
