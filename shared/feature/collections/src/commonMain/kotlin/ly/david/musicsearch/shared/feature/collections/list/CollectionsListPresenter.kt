package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.topappbar.SelectionState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CollectionListPresenter(
    private val screen: CollectionListScreen,
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val collectionRepository: CollectionRepository,
    private val externalScope: CoroutineScope,
) : Presenter<CollectionsListUiState> {
    @Composable
    override fun present(): CollectionsListUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val showLocal by appPreferences.showLocalCollections.collectAsRetainedState(true)
        val showRemote by appPreferences.showRemoteCollections.collectAsRetainedState(true)
        val sortOption by appPreferences.collectionSortOption.collectAsRetainedState(
            CollectionSortOption.ALPHABETICALLY,
        )
        val listItems by rememberRetained(showLocal, showRemote, query, sortOption) {
            mutableStateOf(
                getAllCollections(
                    query = query,
                    showLocal = showLocal,
                    showRemote = showRemote,
                    sortOption = sortOption,
                    entityIdToCheckExists = null,
                ),
            )
        }
        val count by collectionRepository.observeCountOfLocalCollections().collectAsRetainedState(0)
        val selectionState = rememberSelectionState(
            totalCount = count,
        )
        val lazyListState = rememberLazyListState()
        var firstActionableResult: ActionableResult? by remember { mutableStateOf(null) }
        var secondActionableResult: ActionableResult? by remember { mutableStateOf(null) }

        var oneShotNewCollectionId: String? by rememberRetained {
            mutableStateOf(screen.newCollectionId)
        }
        var oneShotNewCollectionName: String? by rememberRetained {
            mutableStateOf(screen.newCollectionName)
        }
        var oneShotNewCollectionType: MusicBrainzEntityType? by rememberRetained {
            mutableStateOf(screen.newCollectionEntity)
        }
        LaunchedEffect(Unit) {
            val id = oneShotNewCollectionId
            val name = oneShotNewCollectionName ?: return@LaunchedEffect
            val entity = oneShotNewCollectionType ?: return@LaunchedEffect
            createCollection(
                newCollection = CreateNewCollectionResult.NewCollection(
                    id = id,
                    name = name,
                    entity = entity,
                ),
            )
            oneShotNewCollectionId = null
            oneShotNewCollectionName = null
            oneShotNewCollectionType = null
        }

        fun eventSink(event: CollectionsListUiEvent) {
            when (event) {
                is CollectionsListUiEvent.UpdateShowLocal -> {
                    appPreferences.setShowLocalCollections(event.show)
                }

                is CollectionsListUiEvent.UpdateShowRemote -> {
                    appPreferences.setShowRemoteCollections(event.show)
                }

                is CollectionsListUiEvent.UpdateSortOption -> {
                    appPreferences.setCollectionSortOption(event.sortOption)
                }

                is CollectionsListUiEvent.CreateNewCollection -> {
                    val name = event.newCollection.name
                    val entity = event.newCollection.entity
                    createCollection(
                        newCollection = CreateNewCollectionResult.NewCollection(
                            name = name,
                            entity = entity,
                        ),
                    )
                }

                is CollectionsListUiEvent.GoToCollection -> {
                    navigator.goTo(CollectionScreen(event.id))
                }

                CollectionsListUiEvent.MarkSelectedItemsAsDeleted -> {
                    firstActionableResult = collectionRepository.markDeletedCollections(
                        collectionIds = selectionState.selectedIds,
                    )
                    selectionState.clearSelection()
                }

                CollectionsListUiEvent.UnMarkItemsAsDeleted -> {
                    collectionRepository.unMarkDeletedCollections()
                }

                is CollectionsListUiEvent.DeleteItemsMarkedAsDeleted -> {
                    externalScope.launch {
                        secondActionableResult = collectionRepository.deleteCollectionsMarkedForDeletion()
                    }
                }
            }
        }

        return CollectionsListUiState(
            topAppBarFilterState = topAppBarFilterState,
            showLocal = showLocal,
            showRemote = showRemote,
            sortOption = sortOption,
            selectionState = selectionState,
            lazyListState = lazyListState,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            firstActionableResult = firstActionableResult,
            secondActionableResult = secondActionableResult,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CollectionsListUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val showLocal: Boolean = true,
    val showRemote: Boolean = true,
    val sortOption: CollectionSortOption = CollectionSortOption.ALPHABETICALLY,
    val selectionState: SelectionState = SelectionState(),
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val firstActionableResult: ActionableResult? = null,
    val secondActionableResult: ActionableResult? = null,
    val eventSink: (CollectionsListUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CollectionsListUiEvent : CircuitUiEvent {
    data class UpdateShowLocal(val show: Boolean) : CollectionsListUiEvent
    data class UpdateShowRemote(val show: Boolean) : CollectionsListUiEvent
    data class UpdateSortOption(val sortOption: CollectionSortOption) : CollectionsListUiEvent
    data class CreateNewCollection(val newCollection: CreateNewCollectionResult.NewCollection) : CollectionsListUiEvent
    data class GoToCollection(
        val id: String,
    ) : CollectionsListUiEvent

    data object MarkSelectedItemsAsDeleted : CollectionsListUiEvent
    data object UnMarkItemsAsDeleted : CollectionsListUiEvent
    data object DeleteItemsMarkedAsDeleted : CollectionsListUiEvent
}
