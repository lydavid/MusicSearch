package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType

class RelationDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.relationQueries

    private fun insert(relation: RelationWithOrder) {
        relation.run {
            transacter.insert(
                entity_id = id,
                linked_entity_id = linkedEntityId,
                linked_entity = linkedEntity,
                order = order,
                label = label,
                name = name,
                disambiguation = disambiguation,
                attributes = attributes,
                additional_info = additionalInfo,
            )
        }
    }

    fun insertAll(relations: List<RelationWithOrder>?) {
        transacter.transaction {
            relations?.forEach { relation ->
                insert(relation)
            }
        }
    }

    fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String = "%%",
    ): PagingSource<Int, RelationListItemModel> = QueryPagingSource(
        countQuery = transacter.countEntityRelationshipsExcludingUrls(
            entityId = entityId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEntityRelationshipsExcludingUrls(
                entityId = entityId,
                query = query,
                limit = limit,
                offset = offset,
                mapper = ::toRelationListItemModel,
            )
        },
    )

    fun deleteRelationshipsExcludingUrlsByEntity(
        entityId: String,
    ) {
        transacter.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }

    fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel> {
        return transacter.getEntityUrlRelationships(
            entityId = entityId,
            // We filter URLs in the presentation layer
            query = "%%",
            mapper = ::toRelationListItemModel,
        ).executeAsList()
    }

    fun deleteUrlRelationshipsByEntity(
        entityId: String,
    ) {
        transacter.deleteUrlRelationshipssByEntity(entityId)
    }

    private fun toRelationListItemModel(
        linkedEntityId: String,
        linkedEntity: MusicBrainzEntity,
        order: Int,
        label: String,
        name: String,
        disambiguation: String?,
        attributes: String?,
        additionalInfo: String?,
    ) = RelationListItemModel(
        id = "${linkedEntityId}_$order",
        linkedEntityId = linkedEntityId,
        linkedEntity = linkedEntity,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo,
    )

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<CountOfEachRelationshipType>> =
        transacter.countOfEachRelationshipType(entityId)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
}
