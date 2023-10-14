package ly.david.musicsearch.domain.relation

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.listitem.RelationWithOrder
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount

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

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<RelationTypeCount>>
}
