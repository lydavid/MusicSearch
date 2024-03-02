package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.collection.usecase.GetCollection
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.screens.CollectionScreen
import ly.david.musicsearch.shared.screens.DetailsScreen

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollectionUseCase: GetCollection,
    private val incrementLookupHistory: IncrementLookupHistory,
) : Presenter<CollectionUiState> {
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.id

        var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            val _collection = getCollectionUseCase(collectionId)
            collection = _collection
            incrementLookupHistory(
                LookupHistory(
                    mbid = collectionId,
                    title = _collection.name,
                    entity = MusicBrainzEntity.COLLECTION,
                ),
            )
            isRemote = _collection.isRemote
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
                                event.entity,
                                event.id,
                                event.title,
                            ),
                        ),
                    )
                }

                is CollectionUiEvent.DeleteItem -> TODO()
                is CollectionUiEvent.MarkItemForDeletion -> TODO()
                is CollectionUiEvent.UnMarkItemForDeletion -> TODO()
                is CollectionUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return CollectionUiState(
            collection = collection,
            query = query,
            eventSink = ::eventSink,
        )
    }

//    private var recordedLookup = false
//
//    fun getCollection(collectionId: String): CollectionListItemModel {
//
//
//        if (!recordedLookup) {
//            incrementLookupHistory(
//                LookupHistory(
//                    mbid = collectionId,
//                    title = collection.name,
//                    entity = MusicBrainzEntity.COLLECTION,
//                ),
//            )
//            recordedLookup = true
//        }
//
//        return collection
//    }
}
