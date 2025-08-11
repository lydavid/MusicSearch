package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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
        entity: MusicBrainzEntity = MusicBrainzEntity.URL,
    ): ImmutableList<RelationListItemModel>

    fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity = MusicBrainzEntity.URL,
    )

    suspend fun insertAllRelations(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
        now: Instant,
    )

    fun observeEntityRelationships(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity> = relatableEntities subtract setOf(MusicBrainzEntity.URL),
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>>

    fun observeCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>

    fun observeLastUpdated(entityId: String): Flow<Instant?>
}
