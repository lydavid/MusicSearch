package ly.david.musicsearch.shared.feature.collections.add

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
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import kotlin.time.Clock

internal class AddToCollectionPresenter(
    private val screen: AddToCollectionScreen,
    private val navigator: Navigator,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val collectionRepository: CollectionRepository,
    private val clock: Clock,
) : Presenter<AddToCollectionUiState> {
    @Composable
    override fun present(): AddToCollectionUiState {
        val scope = rememberCoroutineScope()
        var intermediateFeedback: Feedback<EditACollectionFeedback>? by remember { mutableStateOf(null) }
        val listItems: Flow<PagingData<CollectionListItemModel>> by rememberRetained {
            mutableStateOf(
                getAllCollections(
                    entityType = screen.entityType,
                    showLocal = true,
                    showRemote = true,
                    entityIdToCheckExists = if (screen.collectableIds.size == 1) {
                        screen.collectableIds.first()
                    } else {
                        null
                    },
                ),
            )
        }

        fun eventSink(event: AddToCollectionUiEvent) {
            when (event) {
                is AddToCollectionUiEvent.CreateNewCollection -> {
                    val name = event.newCollection.name
                    val entity = event.newCollection.entity
                    createCollection(
                        newCollection = CreateNewCollectionResult.NewCollection(
                            name = name,
                            entity = entity,
                        ),
                    )
                }

                is AddToCollectionUiEvent.AddToCollection -> {
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

        return AddToCollectionUiState(
            defaultEntityType = screen.entityType,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            feedback = intermediateFeedback,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class AddToCollectionUiState(
    val defaultEntityType: MusicBrainzEntityType,
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val feedback: Feedback<EditACollectionFeedback>?,
    val eventSink: (AddToCollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface AddToCollectionUiEvent : CircuitUiEvent {
    data class CreateNewCollection(val newCollection: CreateNewCollectionResult.NewCollection) : AddToCollectionUiEvent
    data class AddToCollection(
        val collectionId: String,
    ) : AddToCollectionUiEvent
}
