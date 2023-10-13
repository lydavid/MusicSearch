package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType
import lydavidmusicsearchdatadatabase.Relation

class RelationDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.relationQueries

    fun insert(relation: Relation) {
        transacter.insertEntity(relation)
    }

    fun insertAll(relations: List<Relation>?) {
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
    ) { limit, offset ->
        transacter.getEntityRelationshipsExcludingUrls(
            entityId = entityId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::toRelationListItemModel
        )
    }

    fun getEntityUrlRelationships(
        entityId: String,
    ): List<RelationListItemModel> {
        return transacter.getEntityUrlRelationships(
            entityId = entityId,
            query = "%%", // TODO: either filter here or with Kotlin like before
            mapper = ::toRelationListItemModel
        ).executeAsList()
    }

    private fun toRelationListItemModel(
        entity_id: String,
        linked_entity_id: String,
        linked_entity: MusicBrainzEntity,
        order: Int,
        label: String,
        name: String,
        disambiguation: String?,
        attributes: String?,
        additional_info: String?,
    ) = RelationListItemModel(
        id = "${linked_entity_id}_$order",
        linkedEntityId = linked_entity_id,
        linkedEntity = linked_entity,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additional_info
    )

    fun deleteRelationshipsExcludingUrlsByEntity(
        entityId: String,
    ) {
        transacter.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<CountOfEachRelationshipType>> =
        transacter.countOfEachRelationshipType(entityId)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
}
