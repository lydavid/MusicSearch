package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

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

    suspend fun insertAllRelationsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
    )

    fun observeEntityRelationshipsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
        query: String,
    ): Flow<PagingData<RelationListItemModel>>

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>
}
