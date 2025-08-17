package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.relatableEntities

interface RelationRepository {
    fun visited(entityId: String): Boolean

    fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
        lastUpdated: Instant = Clock.System.now(),
    )

    fun getRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntityType = MusicBrainzEntityType.URL,
    ): ImmutableList<RelationListItemModel>

    fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntityType = MusicBrainzEntityType.URL,
    )

    suspend fun insertAllRelations(
        entity: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        now: Instant,
    )

    fun observeEntityRelationships(
        entity: MusicBrainzEntityType,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType> = relatableEntities subtract setOf(MusicBrainzEntityType.URL),
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>>

    fun observeCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>

    fun observeLastUpdated(entityId: String): Flow<Instant?>
}
