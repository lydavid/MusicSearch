package ly.david.musicsearch.domain.relation

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.listitem.RelationWithOrder
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount

interface RelationRepository {
    fun hasUrlsBeenSavedFor(entityId: String): Boolean

    fun insertAllUrlRelations(
        entityId: String,
        relationWithOrderList: List<RelationWithOrder>?
    )

    suspend fun insertAllRelationsExcludingUrls(
        entity: MusicBrainzEntity,
        entityId: String,
    )

    fun insertRelations(entityId: String, relationMusicBrainzModels: List<RelationWithOrder>?)
    fun hasRelationsBeenSavedFor(entityId: String): Boolean
    fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String,
    ): PagingSource<Int, RelationListItemModel>

    fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel>

    fun deleteEntityRelationships(
        entityId: String,
    )

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>
}
