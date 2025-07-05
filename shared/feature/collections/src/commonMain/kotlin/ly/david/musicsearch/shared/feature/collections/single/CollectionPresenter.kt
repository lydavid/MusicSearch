package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.list.EntitiesListPresenter
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.list.getTotalLocalCount
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.SelectionState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.toTab

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollection: GetCollection,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val loginPresenter: LoginPresenter,
    private val entitiesListPresenter: EntitiesListPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val collectionRepository: CollectionRepository,
) : Presenter<CollectionUiState>, RecordVisit {

    @Suppress("CyclomaticComplexMethod")
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.collectionId

        var collection: CollectionListItemModel? by rememberRetained { mutableStateOf(null) }
        var title: String by rememberSaveable { mutableStateOf("") }
        var firstActionableResult: ActionableResult? by remember { mutableStateOf(null) }
        var secondActionableResult: ActionableResult? by remember { mutableStateOf(null) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }

        val loginUiState = loginPresenter.present()

        val entitiesListUiState = entitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink

        val selectionState = rememberSelectionState(
            totalCount = entitiesListUiState.getTotalLocalCount(collection?.entity),
        )

        var oneShotNewCollectableId: String? by rememberRetained {
            mutableStateOf(screen.collectableId)
        }

        LaunchedEffect(Unit) {
            val nonNullCollection = getCollection(collectionId) ?: return@LaunchedEffect
            collection = nonNullCollection
            isRemote = nonNullCollection.isRemote
            title = nonNullCollection.name

            collectionRepository.addToCollection(
                collectionId = nonNullCollection.id,
                entity = nonNullCollection.entity,
                entityIds = oneShotNewCollectableId?.run { setOf(this) } ?: return@LaunchedEffect,
            )
            oneShotNewCollectableId = null
        }

        RecordVisit(
            mbid = collectionId,
            title = title,
            entity = MusicBrainzEntity.COLLECTION,
            searchHint = "",
        )

        LaunchedEffect(
            key1 = query,
        ) {
            val tab = collection?.entity?.toTab() ?: return@LaunchedEffect
            val browseMethod = BrowseMethod.ByEntity(
                entityId = collectionId,
                entity = MusicBrainzEntity.COLLECTION,
            )
            entitiesListEventSink(
                EntitiesListUiEvent.Get(
                    tab = tab,
                    browseMethod = browseMethod,
                    query = query,
                    isRemote = isRemote,
                ),
            )
        }

        fun eventSink(event: CollectionUiEvent) {
            when (event) {
                is CollectionUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CollectionUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }

                is CollectionUiEvent.MarkSelectedItemsAsDeleted -> {
                    firstActionableResult = collectionRepository.markDeletedFromCollection(
                        collection = collection ?: return,
                        collectableIds = selectionState.selectedIds,
                    )
                    selectionState.clearSelection()
                }

                is CollectionUiEvent.UnMarkItemsAsDeleted -> {
                    collectionRepository.unMarkDeletedFromCollection(
                        collectionId = collection?.id ?: return,
                    )
                }
            }
        }

        suspend fun suspendEventSink(event: SuspendCollectionUiEvent) {
            when (event) {
                is SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted -> {
                    // We cannot launch a new scope if we want to run this as part of the cancellation of the parent scope.
                    secondActionableResult = collectionRepository.deleteFromCollection(
                        collection = collection ?: return,
                    )
                }
            }
        }

        return CollectionUiState(
            title = title,
            collection = collection,
            url = getMusicBrainzUrl(MusicBrainzEntity.COLLECTION, screen.collectionId),
            firstActionableResult = firstActionableResult,
            secondActionableResult = secondActionableResult,
            topAppBarFilterState = topAppBarFilterState,
            selectionState = selectionState,
            loginUiState = loginUiState,
            entitiesListUiState = entitiesListUiState,
            eventSink = ::eventSink,
            suspendEventSink = ::suspendEventSink,
        )
    }
}

@Stable
internal data class CollectionUiState(
    val title: String,
    val collection: CollectionListItemModel?,
    val firstActionableResult: ActionableResult?,
    val secondActionableResult: ActionableResult?,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val url: String,
    val selectionState: SelectionState,
    val loginUiState: LoginUiState,
    val entitiesListUiState: EntitiesListUiState,
    val eventSink: (CollectionUiEvent) -> Unit,
    val suspendEventSink: suspend (SuspendCollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionUiEvent : CircuitUiEvent {
    data object NavigateUp : CollectionUiEvent

    data object MarkSelectedItemsAsDeleted : CollectionUiEvent

    data object UnMarkItemsAsDeleted : CollectionUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : CollectionUiEvent
}

internal sealed interface SuspendCollectionUiEvent : CircuitUiEvent {
    data object DeleteItemsMarkedAsDeleted : SuspendCollectionUiEvent
}
