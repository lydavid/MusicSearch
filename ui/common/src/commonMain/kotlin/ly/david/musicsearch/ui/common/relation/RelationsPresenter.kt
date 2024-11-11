package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships

interface RelationsPresenter : Presenter<RelationsUiState>

class RelationsPresenterImpl(
    private val getEntityRelationships: GetEntityRelationships,
) : RelationsPresenter {
    @Composable
    override fun present(): RelationsUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        val relationListItems: Flow<PagingData<RelationListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getEntityRelationships(
                    entityId = id,
                    entity = entity,
                    query = query,
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        fun eventSink(event: RelationsUiEvent) {
            when (event) {
                is RelationsUiEvent.GetRelations -> {
                    id = event.byEntityId
                    entity = event.byEntity
                }

                is RelationsUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return RelationsUiState(
            lazyPagingItems = relationListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}
