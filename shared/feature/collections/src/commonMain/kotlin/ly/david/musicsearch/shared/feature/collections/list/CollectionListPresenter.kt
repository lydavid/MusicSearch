package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.collections.create.NewCollection
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CollectionListPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val deleteCollection: DeleteCollection,
) : Presenter<CollectionListUiState> {
    @Composable
    override fun present(): CollectionListUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val topAppBarEditState = rememberTopAppBarEditState()
        val scope = rememberCoroutineScope()
        val query = topAppBarFilterState.filterText
        val showLocal by appPreferences.showLocalCollections.collectAsRetainedState(true)
        val showRemote by appPreferences.showRemoteCollections.collectAsRetainedState(true)
        val sortOption by appPreferences.collectionSortOption.collectAsRetainedState(
            CollectionSortOption.ALPHABETICALLY,
        )
        val listItems by rememberRetained(showLocal, showRemote, query, sortOption) {
            mutableStateOf(
                getAllCollections(
                    showLocal = showLocal,
                    showRemote = showRemote,
                    query = query,
                    sortOption = sortOption,
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        var actionableResult: ActionableResult? by remember { mutableStateOf(null) }

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

                is CollectionListUiEvent.DeleteCollection -> {
                    scope.launch {
                        actionableResult = deleteCollection(
                            event.id,
                            event.name,
                        )
                    }
                }
            }
        }

        return CollectionListUiState(
            topAppBarFilterState = topAppBarFilterState,
            topAppBarEditState = topAppBarEditState,
            showLocal = showLocal,
            showRemote = showRemote,
            sortOption = sortOption,
            lazyListState = lazyListState,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            actionableResult = actionableResult,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CollectionListUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val topAppBarEditState: TopAppBarEditState = TopAppBarEditState(),
    val showLocal: Boolean,
    val showRemote: Boolean,
    val sortOption: CollectionSortOption,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val lazyListState: LazyListState,
    val actionableResult: ActionableResult?,
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

    data class DeleteCollection(
        val id: String,
        val name: String,
    ) : CollectionListUiEvent
}
