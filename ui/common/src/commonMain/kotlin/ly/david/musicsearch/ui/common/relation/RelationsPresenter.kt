package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.relatableEntities
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
        var relatedEntities: Set<MusicBrainzEntity> by rememberSaveable {
            mutableStateOf(
                relatableEntities subtract setOf(MusicBrainzEntity.URL),
            )
        }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getEntityRelationships(
                    entityId = id,
                    entity = entity,
                    relatedEntities = relatedEntities,
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
                    relatedEntities = event.relatedEntities
                }

                is RelationsUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return RelationsUiState(
            pagingDataFlow = pagingDataFlow,
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}
