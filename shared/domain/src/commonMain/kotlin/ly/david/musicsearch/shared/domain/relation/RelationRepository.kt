package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.relatableEntities
import kotlin.time.Clock
import kotlin.time.Instant

interface RelationRepository {
    fun visited(entityId: String): Boolean

    /**
     * See [observeEntityRelationships] for inserting the rest of the relations not inserted by this.
     */
    fun insertRelations(
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

    /**
     * Returns latest [relatedEntityTypes] relationships of [entity],
     * preferring local cache if it exists, otherwise fetching from remote, and caching the result.
     *
     * By default, we exclude [MusicBrainzEntityType.URL] which are fetched and shown separately.
     */
    fun observeEntityRelationships(
        entity: MusicBrainzEntity,
        relatedEntityTypes: Set<MusicBrainzEntityType> = relatableEntities subtract setOf(MusicBrainzEntityType.URL),
        query: String,
        lastUpdated: Instant,
    ): Flow<PagingData<ListItemModel>>

    fun observeCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>

    fun observeLastUpdated(entityId: String): Flow<Instant?>
}
