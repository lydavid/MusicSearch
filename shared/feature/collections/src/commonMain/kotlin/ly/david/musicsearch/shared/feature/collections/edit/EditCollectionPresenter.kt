package ly.david.musicsearch.shared.feature.collections.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.withTime
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.EditCollectionScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import kotlin.time.Clock

internal class EditCollectionPresenter(
    private val screen: EditCollectionScreen,
    private val navigator: Navigator,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val collectionRepository: CollectionRepository,
    private val clock: Clock,
) : Presenter<EditCollectionUiState> {
    @Composable
    override fun present(): EditCollectionUiState {
        val scope = rememberCoroutineScope()
        var intermediateFeedback: Feedback<EditACollectionFeedback>? by remember { mutableStateOf(null) }
        val listItems: Flow<PagingData<CollectionListItemModel>> by rememberRetained {
            mutableStateOf(
                getAllCollections(
                    entityType = screen.entityType,
                    showLocal = true,
                    showRemote = true,
                    entityIdsToCheckExist = screen.collectableIds.toSet(),
                ),
            )
        }

        fun eventSink(event: EditCollectionUiEvent) {
            when (event) {
                is EditCollectionUiEvent.CreateNewCollection -> {
                    val name = event.newCollection.name
                    val entity = event.newCollection.entity
                    createCollection(
                        newCollection = CreateNewCollectionResult.NewCollection(
                            name = name,
                            entity = entity,
                        ),
                    )
                }

                is EditCollectionUiEvent.AddToCollection -> {
                    scope.launch {
                        collectionRepository.addToCollection(
                            collectionId = event.collectionId,
                            entityType = screen.entityType,
                            entityIds = screen.collectableIds,
                        ).collect { feedback ->
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
                                        SnackbarPopResultV2(
                                            feedback = feedback.withTime(clock.now()),
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        return EditCollectionUiState(
            defaultEntityType = screen.entityType,
            numberOfItemsToAddToCollection = screen.collectableIds.size,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            feedback = intermediateFeedback,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class EditCollectionUiState(
    val defaultEntityType: MusicBrainzEntityType,
    val numberOfItemsToAddToCollection: Int,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val feedback: Feedback<EditACollectionFeedback>?,
    val eventSink: (EditCollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface EditCollectionUiEvent : CircuitUiEvent {
    data class CreateNewCollection(val newCollection: CreateNewCollectionResult.NewCollection) : EditCollectionUiEvent
    data class AddToCollection(
        val collectionId: String,
    ) : EditCollectionUiEvent
}
