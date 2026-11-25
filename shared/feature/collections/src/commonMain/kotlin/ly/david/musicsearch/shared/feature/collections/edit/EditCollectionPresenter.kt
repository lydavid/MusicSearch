package ly.david.musicsearch.shared.feature.collections.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.collection.PendingEdit
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.withTime
import ly.david.musicsearch.shared.domain.listitem.CollectionContainsEntities
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.screen.EditCollectionScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import kotlin.time.Clock

internal class EditCollectionPresenter(
    private val screen: EditCollectionScreen,
    private val navigator: Navigator,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val collectionRepository: CollectionRepository,
    private val appPreferences: AppPreferences,
    private val clock: Clock,
) : Presenter<EditCollectionUiState> {

    @Composable
    override fun present(): EditCollectionUiState {
        val coroutineScope = rememberCoroutineScope()
        var intermediateFeedback: Feedback<EditACollectionFeedback>? by remember { mutableStateOf(null) }
        val filterState = rememberTopAppBarFilterState(
            transitionType = TopAppBarFilterState.TransitionType.Horizontal,
        )
        val query = filterState.filterText
        val collectableIds = screen.collectableIds
        val entityType = screen.entityType
        val listItems: Flow<PagingData<CollectionListItemModel>> by rememberRetained(query) {
            mutableStateOf(
                getAllCollections(
                    entityType = entityType,
                    showLocal = true,
                    showRemote = true,
                    query = query,
                    entityIdsToCheckExist = collectableIds.toSet(),
                ),
            )
        }
        val boldUnvisited by appPreferences.boldUnvisited.collectAsRetainedState(true)
        val confirmChanges by appPreferences.confirmChanges.collectAsRetainedState(true)

        // Use mutableStateMapOf so Compose detects in-place mutations (add/remove) and triggers recomposition.
        var pendingEdits by rememberRetained { mutableStateOf(mutableStateMapOf<String, CollectionListItemModel>()) }

        fun setPending(
            collection: CollectionListItemModel,
            pendingEdit: PendingEdit,
        ) {
            pendingEdits[collection.id] = collection.copy(
                pendingEdit = pendingEdit,
            )
        }

        fun clearPending(collection: CollectionListItemModel) {
            pendingEdits.remove(collection.id)
        }

        fun handleFeedback(feedback: Feedback<EditACollectionFeedback>) {
            when (feedback) {
                is Feedback.Loading<*>,
                is Feedback.Actionable<*>,
                -> {
                    intermediateFeedback = feedback
                }

                is Feedback.Error<*>,
                is Feedback.Success<*>,
                -> {
                    navigator.pop(
                        SnackbarPopResult(
                            feedback = feedback.withTime(clock.now()),
                        ),
                    )
                }
            }
        }

        fun addToCollection(collection: CollectionListItemModel) {
            coroutineScope.launch {
                collectionRepository.addToCollection(
                    collection = collection,
                    entityType = entityType,
                    collectableIds = collectableIds,
                ).collect { feedback ->
                    handleFeedback(feedback)
                }
            }
        }

        fun deleteFromCollection(collection: CollectionListItemModel) {
            coroutineScope.launch {
                collectionRepository.deleteFromCollectionImmediately(
                    collection = collection,
                    collectableIds = collectableIds,
                ).collect { feedback ->
                    handleFeedback(feedback)
                }
            }
        }

        fun applyAllEdits() {
            coroutineScope.launch {
                collectionRepository.applyAllEdits(
                    pendingEdits = pendingEdits,
                    entityType = entityType,
                    collectableIds = collectableIds,
                ).collect { feedback ->
                    handleFeedback(feedback)
                }
            }
        }

        return EditCollectionUiState(
            defaultEntityType = entityType,
            numberOfItemsToAddToCollection = collectableIds.size,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            feedback = intermediateFeedback,
            boldUnvisited = boldUnvisited,
            confirmChanges = confirmChanges,
            filterState = filterState,
            pendingEdits = pendingEdits,
            eventSink = eventSink(
                confirmChanges = confirmChanges,
                addToCollection = ::addToCollection,
                deleteFromCollection = ::deleteFromCollection,
                setPending = ::setPending,
                clearPending = ::clearPending,
                applyAllEdits = ::applyAllEdits,
            ),
        )
    }

    private fun eventSink(
        confirmChanges: Boolean,
        addToCollection: (CollectionListItemModel) -> Unit,
        deleteFromCollection: (CollectionListItemModel) -> Unit,
        setPending: (CollectionListItemModel, PendingEdit) -> Unit,
        clearPending: (CollectionListItemModel) -> Unit,
        applyAllEdits: () -> Unit,
    ): (EditCollectionUiEvent) -> Unit = { event ->
        when (event) {
            is EditCollectionUiEvent.CreateNewCollection -> {
                createCollection(
                    newCollection = event.newCollection,
                )
            }

            is EditCollectionUiEvent.ClickCollection -> {
                clickCollection(
                    confirmChanges = confirmChanges,
                    addToCollection = addToCollection,
                    deleteFromCollection = deleteFromCollection,
                    collection = event.collection,
                    setPending = setPending,
                    clearPending = clearPending,
                )
            }

            is EditCollectionUiEvent.ApplyChanges -> {
                applyAllEdits()
            }

            EditCollectionUiEvent.Dismiss -> {
                navigator.pop(
                    result = SnackbarPopResult<Feedback.Success<EditACollectionFeedback>>(null),
                )
            }
        }
    }

    private fun clickCollection(
        confirmChanges: Boolean,
        addToCollection: (CollectionListItemModel) -> Unit,
        deleteFromCollection: (CollectionListItemModel) -> Unit,
        collection: CollectionListItemModel,
        setPending: (CollectionListItemModel, PendingEdit) -> Unit,
        clearPending: (CollectionListItemModel) -> Unit,
    ) {
        val dbContainsEntities = collection.containsEntities
        val currentPendingEdit = collection.pendingEdit
        if (!confirmChanges) {
            when (dbContainsEntities) {
                CollectionContainsEntities.None,
                CollectionContainsEntities.Some,
                null,
                -> {
                    addToCollection(collection)
                }

                CollectionContainsEntities.All -> {
                    deleteFromCollection(collection)
                }
            }
        } else {
            togglePending(
                collection = collection,
                dbContainsEntities = dbContainsEntities,
                currentPendingEdit = currentPendingEdit,
                setPending = setPending,
                clearPending = clearPending,
            )
        }
    }

    private fun togglePending(
        collection: CollectionListItemModel,
        dbContainsEntities: CollectionContainsEntities?,
        currentPendingEdit: PendingEdit?,
        setPending: (CollectionListItemModel, PendingEdit) -> Unit,
        clearPending: (CollectionListItemModel) -> Unit,
    ) {
        when (currentPendingEdit) {
            null -> {
                when (dbContainsEntities) {
                    CollectionContainsEntities.All ->
                        setPending(collection, PendingEdit.Remove)

                    CollectionContainsEntities.None,
                    CollectionContainsEntities.Some,
                    null,
                    ->
                        setPending(collection, PendingEdit.Add)
                }
            }

            PendingEdit.Add -> {
                clearPending(collection)
                when (dbContainsEntities) {
                    CollectionContainsEntities.All,
                    CollectionContainsEntities.Some,
                    null,
                    -> {
                        setPending(collection, PendingEdit.Remove)
                    }

                    CollectionContainsEntities.None -> {
                        // pending edit is now remove, which means we're back to original state
                    }
                }
            }

            PendingEdit.Remove -> {
                clearPending(collection)
                when (dbContainsEntities) {
                    CollectionContainsEntities.None,
                    CollectionContainsEntities.Some,
                    null,
                    -> {
                        setPending(collection, PendingEdit.Add)
                    }

                    CollectionContainsEntities.All -> {
                        // pending edit is now add, which means we're back to original state
                    }
                }
            }
        }
    }
}

@Stable
internal data class EditCollectionUiState(
    val defaultEntityType: MusicBrainzEntityType,
    val numberOfItemsToAddToCollection: Int,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val feedback: Feedback<EditACollectionFeedback>? = null,
    val boldUnvisited: Boolean = true,
    val confirmChanges: Boolean = true,
    val filterState: TopAppBarFilterState = TopAppBarFilterState(),
    val pendingEdits: Map<String, CollectionListItemModel> = mapOf(),
    val eventSink: (EditCollectionUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface EditCollectionUiEvent : CircuitUiEvent {
    data class CreateNewCollection(val newCollection: CreateNewCollectionResult.NewCollection) : EditCollectionUiEvent
    data class ClickCollection(
        val collection: CollectionListItemModel,
    ) : EditCollectionUiEvent

    data object ApplyChanges : EditCollectionUiEvent

    data object Dismiss : EditCollectionUiEvent
}
