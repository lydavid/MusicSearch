package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder
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
                is_forward_direction = isForwardDirection,
                begin = lifeSpan.begin,
                end = lifeSpan.end,
                ended = lifeSpan.ended,
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

    fun getEntityRelationships(
        entityId: String,
        query: String = "%%",
        relatedEntities: Set<MusicBrainzEntity>,
    ): PagingSource<Int, RelationListItemModel> = QueryPagingSource(
        countQuery = transacter.countEntityRelationships(
            entityId = entityId,
            relatedEntities = relatedEntities,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEntityRelationships(
                entityId = entityId,
                relatedEntities = relatedEntities,
                query = query,
                limit = limit,
                offset = offset,
                mapper = ::mapToRelationListItemModel,
            )
        },
    )

    fun deleteRelationshipsExcludingUrlsByEntity(
        entityId: String,
        relatedEntities: Set<MusicBrainzEntity>,
    ) {
        transacter.deleteRelationships(
            entityId = entityId,
            relatedEntities = relatedEntities,
        )
    }

    fun getRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity,
    ): List<RelationListItemModel> {
        return transacter.getRelationshipsByType(
            entityId = entityId,
            entityType = entity,
            // We filter URLs in the presentation layer
            query = "%%",
            mapper = ::mapToRelationListItemModel,
        ).executeAsList()
    }

    fun deleteRelationshipsByType(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        transacter.deleteRelationshipsByType(
            entityId = entityId,
            entityType = entity,
        )
    }

    private fun mapToRelationListItemModel(
        linkedEntityId: String,
        linkedEntity: MusicBrainzEntity,
        order: Int,
        label: String,
        name: String,
        disambiguation: String?,
        attributes: String?,
        additionalInfo: String?,
        visited: Boolean?,
        isForwardDirection: Boolean?,
        begin: String?,
        end: String?,
        ended: Boolean?,
        thumbnailUrl: String?,
        placeholderKey: Long?,
    ) = RelationListItemModel(
        id = "${linkedEntityId}_$order",
        linkedEntityId = linkedEntityId,
        linkedEntity = linkedEntity,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo,
        visited = visited == true || linkedEntity == MusicBrainzEntity.URL,
        isForwardDirection = isForwardDirection,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        imageUrl = thumbnailUrl,
        placeholderKey = placeholderKey,
    )

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<CountOfEachRelationshipType>> =
        transacter.countOfEachRelationshipType(entityId)
            .asFlow()
            .mapToList(coroutineDispatchers.io)
}
