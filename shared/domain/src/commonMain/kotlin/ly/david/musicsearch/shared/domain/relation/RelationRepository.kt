package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.relatableEntities

interface RelationRepository {
    fun visited(entityId: String): Boolean

    fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
    )

    fun getRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity = MusicBrainzEntity.URL,
    ): List<RelationListItemModel>

    fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity = MusicBrainzEntity.URL,
    )

    suspend fun insertAllRelations(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
    )

    fun observeEntityRelationships(
        entity: MusicBrainzEntity,
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity> = relatableEntities subtract setOf(MusicBrainzEntity.URL),
        query: String,
    ): Flow<PagingData<RelationListItemModel>>

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>
}
