package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.domain.collection.CollectionRepository
import ly.david.musicsearch.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.feature.collections.create.NewCollection
import ly.david.ui.common.screen.AddToCollectionScreen

internal class AddToCollectionPresenter(
    private val screen: AddToCollectionScreen,
    private val navigator: Navigator,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
    private val collectionRepository: CollectionRepository,
) : Presenter<AddToCollectionUiState> {
    @Composable
    override fun present(): AddToCollectionUiState {
        val scope = rememberCoroutineScope()
        val lazyPagingItems: LazyPagingItems<CollectionListItemModel> = getAllCollections(
            showLocal = true,
            showRemote = true,
            entity = screen.entity,
        ).collectAsLazyPagingItems()

        fun eventSink(event: AddToCollectionUiEvent) {
            when (event) {
                is AddToCollectionUiEvent.CreateNewCollection -> {
                    val name = event.newCollection.name ?: return
                    val entity = event.newCollection.entity ?: return
                    createCollection(
                        name = name,
                        entity = entity,
                    )
                }

                is AddToCollectionUiEvent.AddToCollection -> {
                    scope.launch {
                        collectionRepository.addToCollection(
                            collectionId = event.id,
                            entity = screen.entity,
                            entityId = screen.id,
                        )
                        navigator.pop()
                    }
                }
            }
        }

        return AddToCollectionUiState(
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class AddToCollectionUiState(
    val lazyPagingItems: LazyPagingItems<CollectionListItemModel>,
    val eventSink: (AddToCollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface AddToCollectionUiEvent : CircuitUiEvent {
    data class CreateNewCollection(val newCollection: NewCollection) : AddToCollectionUiEvent
    data class AddToCollection(
        val id: String,
    ) : AddToCollectionUiEvent
}
