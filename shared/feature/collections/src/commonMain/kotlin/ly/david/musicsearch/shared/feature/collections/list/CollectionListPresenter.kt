package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.feature.collections.create.NewCollection
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CollectionListPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
) : Presenter<CollectionListUiState> {
    @Composable
    override fun present(): CollectionListUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val showLocal by appPreferences.showLocalCollections.collectAsState(true)
        val showRemote by appPreferences.showRemoteCollections.collectAsState(true)
        val sortOption by appPreferences.collectionSortOption.collectAsState(CollectionSortOption.ALPHABETICALLY)
        val lazyPagingItems: LazyPagingItems<CollectionListItemModel> = getAllCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            sortOption = sortOption,
        ).collectAsLazyPagingItems()
        val lazyListState = rememberLazyListState()

        fun eventSink(event: CollectionListUiEvent) {
            when (event) {
                is CollectionListUiEvent.UpdateShowLocal -> {
                    appPreferences.setShowLocalCollections(event.show)
                }

                is CollectionListUiEvent.UpdateShowRemote -> {
                    appPreferences.setShowRemoteCollections(event.show)
                }

                is CollectionListUiEvent.UpdateSortOption -> {
                    appPreferences.setCollectionSortOption(event.sortOption)
                }

                is CollectionListUiEvent.CreateNewCollection -> {
                    val name = event.newCollection.name ?: return
                    val entity = event.newCollection.entity ?: return
                    createCollection(
                        name = name,
                        entity = entity,
                    )
                }

                is CollectionListUiEvent.GoToCollection -> {
                    navigator.goTo(CollectionScreen(event.id))
                }
            }
        }

        return CollectionListUiState(
            topAppBarFilterState = topAppBarFilterState,
            showLocal = showLocal,
            showRemote = showRemote,
            sortOption = sortOption,
            lazyListState = lazyListState,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CollectionListUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val showLocal: Boolean,
    val showRemote: Boolean,
    val sortOption: CollectionSortOption,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val lazyListState: LazyListState,
    val eventSink: (CollectionListUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionListUiEvent : CircuitUiEvent {
    data class UpdateShowLocal(val show: Boolean) : CollectionListUiEvent
    data class UpdateShowRemote(val show: Boolean) : CollectionListUiEvent
    data class UpdateSortOption(val sortOption: CollectionSortOption) : CollectionListUiEvent
    data class CreateNewCollection(val newCollection: NewCollection) : CollectionListUiEvent
    data class GoToCollection(
        val id: String,
    ) : CollectionListUiEvent
}
