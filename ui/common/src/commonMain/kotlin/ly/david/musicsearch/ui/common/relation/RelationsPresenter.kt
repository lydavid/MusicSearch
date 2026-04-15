package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.Header.id
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.relatableEntities
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveCountOfRelationshipsByEntity

interface RelationsPresenter : Presenter<RelationsUiState>

class RelationsPresenterImpl(
    private val getEntityRelationships: GetEntityRelationships,
    private val observeCountOfRelationshipsByEntity: ObserveCountOfRelationshipsByEntity,
) : RelationsPresenter {
    @Composable
    override fun present(): RelationsUiState {
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var relatedEntities: Set<MusicBrainzEntityType> by rememberSaveable {
            mutableStateOf(
                relatableEntities subtract setOf(MusicBrainzEntityType.URL),
            )
        }
        var query by rememberSaveable { mutableStateOf("") }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getEntityRelationships(
                    entity = entity,
                    relatedEntities = relatedEntities,
                    query = query,
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        // Note that we count grouped items together (e.g. a performer, vocal relationship counts as 1).
        // So, this will often not be the same as the count of types of relationships in stats.
        val totalCount by observeCountOfRelationshipsByEntity(
            entityId = entity?.id ?: "",
            relatedEntities = relatedEntities,
            query = "",
        ).collectAsRetainedState(0)
        val filteredCount by observeCountOfRelationshipsByEntity(
            entityId = entity?.id ?: "",
            relatedEntities = relatedEntities,
            query = query,
        ).collectAsRetainedState(0)

        fun eventSink(event: RelationsUiEvent) {
            when (event) {
                is RelationsUiEvent.GetRelations -> {
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
            totalCount = totalCount,
            filteredCount = filteredCount,
            eventSink = { event ->
                eventSink(event)
            },
        )
    }
}
