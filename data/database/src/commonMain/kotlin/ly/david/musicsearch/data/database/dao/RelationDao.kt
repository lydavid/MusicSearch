package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.CountOfEachRelationshipType
import lydavidmusicsearchdatadatabase.Mb_relation

// TODO: can we add annotations without ksp, will it be slow?
class RelationDao(
    database: Database,
//    private val dispatcher: CoroutineDispatcher,
) {
    private val transacter = database.mb_relationQueries

    fun insert(relation: Mb_relation) {
        transacter.insertEntity(relation)
    }

    fun insertAll(relations: List<Mb_relation>) {
        transacter.transaction {
            relations.forEach { relation ->
                insert(relation)
            }
        }
    }

    fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String = "%%",
    ): PagingSource<Int, Mb_relation> {
        return QueryPagingSource(
            countQuery = transacter.countEntityRelationshipsExcludingUrls(
                entityId = entityId,
                query = query,
            ),
            transacter = transacter,
            context = Dispatchers.IO, // TODO: inject so we can swap out
        ) { limit, offset ->
            transacter.getEntityRelationshipsExcludingUrls(
                entityId = entityId,
                query = query,
                limit = limit,
                offset = offset,
            )
        }
    }

    fun getEntityUrlRelationships(
        entityId: String,
    ): List<Mb_relation> {
        return transacter.getEntityUrlRelationships(
            entityId = entityId,
            query = "%%", // TODO: either filter here or with Kotlin like before
        ).executeAsList()
    }

    fun deleteRelationshipsExcludingUrlsByEntity(
        entityId: String,
    ) {
        transacter.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }

    fun getNumberOfRelationsByEntity(entityId: String): Long =
        transacter.getNumberOfRelationsByEntity(entityId).executeAsOne()

    fun getCountOfEachRelationshipType(entityId: String): List<CountOfEachRelationshipType> =
        transacter.countOfEachRelationshipType(entityId).executeAsList()
}
