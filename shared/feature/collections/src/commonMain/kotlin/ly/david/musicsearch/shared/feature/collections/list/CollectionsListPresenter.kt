package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CollectionListPresenter(
    private val screen: CollectionListScreen,
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val deleteCollection: DeleteCollection,
) : Presenter<CollectionsListUiState> {
    @Composable
    override fun present(): CollectionsListUiState {
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

        var oneShotNewCollectionId: String? by rememberRetained {
            mutableStateOf(screen.newCollectionId)
        }
        var oneShotNewCollectionName: String? by rememberRetained {
            mutableStateOf(screen.newCollectionName)
        }
        var oneShotNewCollectionType: MusicBrainzEntity? by rememberRetained {
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

                is CollectionsListUiEvent.DeleteCollection -> {
                    scope.launch {
                        actionableResult = deleteCollection(
                            event.id,
                            event.name,
                        )
                    }
                }
            }
        }

        return CollectionsListUiState(
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
internal data class CollectionsListUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val topAppBarEditState: TopAppBarEditState = TopAppBarEditState(),
    val showLocal: Boolean,
    val showRemote: Boolean,
    val sortOption: CollectionSortOption,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val lazyListState: LazyListState,
    val actionableResult: ActionableResult?,
    val eventSink: (CollectionsListUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionsListUiEvent : CircuitUiEvent {
    data class UpdateShowLocal(val show: Boolean) : CollectionsListUiEvent
    data class UpdateShowRemote(val show: Boolean) : CollectionsListUiEvent
    data class UpdateSortOption(val sortOption: CollectionSortOption) : CollectionsListUiEvent
    data class CreateNewCollection(val newCollection: CreateNewCollectionResult.NewCollection) : CollectionsListUiEvent
    data class GoToCollection(
        val id: String,
    ) : CollectionsListUiEvent

    data class DeleteCollection(
        val id: String,
        val name: String,
    ) : CollectionsListUiEvent
}
