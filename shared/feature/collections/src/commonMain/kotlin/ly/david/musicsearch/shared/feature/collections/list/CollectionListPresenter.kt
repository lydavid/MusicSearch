package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.screens.CollectionScreen

internal class CollectionListPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getAllCollections: GetAllCollections,
    private val createCollection: CreateCollection,
) : Presenter<CollectionListUiState> {
    @Composable
    override fun present(): CollectionListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        val showLocal by appPreferences.showLocalCollections.collectAsState(true)
        val showRemote by appPreferences.showRemoteCollections.collectAsState(true)
        val lazyPagingItems: LazyPagingItems<CollectionListItemModel> = getAllCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
        ).collectAsLazyPagingItems()

        fun eventSink(event: CollectionListUiEvent) {
            when (event) {
                is CollectionListUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is CollectionListUiEvent.UpdateShowLocal -> {
                    appPreferences.setShowLocalCollections(event.show)
                }

                is CollectionListUiEvent.UpdateShowRemote -> {
                    appPreferences.setShowRemoteCollections(event.show)
                }

                is CollectionListUiEvent.CreateCollection -> {
                    val name = event.newCollection.name ?: return
                    val entity = event.newCollection.entity ?: return
                    createCollection(
                        name,
                        entity,
                    )
                }

                is CollectionListUiEvent.ClickCollection -> {
                    navigator.goTo(CollectionScreen(event.id))
                }
            }
        }

        return CollectionListUiState(
            query = query,
            showLocal = showLocal,
            showRemote = showRemote,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}
