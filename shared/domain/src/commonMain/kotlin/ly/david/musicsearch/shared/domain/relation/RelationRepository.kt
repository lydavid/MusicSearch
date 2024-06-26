package ly.david.musicsearch.shared.domain.relation

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.relation.RelationWithOrder
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.relation.RelationTypeCount

interface RelationRepository {
    fun hasUrlsBeenSavedFor(entityId: String): Boolean

    fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?,
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

    fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel>

    fun deleteUrlRelationshipsByEntity(
        entityId: String,
    )

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>
}
