package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    // TODO: using Dispatchers.IO causes our compose UI in relationships stats to flicker
    //  back and forth between two states
    //  it's prob cause they're executed separately
    fun getNumberOfRelationsByEntity(entityId: String): Flow<Long> =
        transacter.getNumberOfRelationsByEntity(entityId)
            .asFlow()
            .mapToOne(Dispatchers.Main)

    fun getCountOfEachRelationshipType(entityId: String): Flow<List<CountOfEachRelationshipType>> =
        transacter.countOfEachRelationshipType(entityId)
            .asFlow()
            .mapToList(Dispatchers.Main)
}
