package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.getCount
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Collection

class CollectionDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.collectionQueries

    fun insertLocal(collection: CollectionListItemModel) {
        collection.run {
            transacter.insert(
                collection = Collection(
                    id = id,
                    is_remote = isRemote,
                    name = name,
                    entity = entity,
                    type = null,
                    type_id = null,
                    entity_count = cachedEntityCount,
                ),
            )
        }
    }

    private fun insertRemote(collection: CollectionMusicBrainzNetworkModel) {
        collection.run {
            transacter.insert(
                Collection(
                    id = id,
                    is_remote = true,
                    name = name,
                    entity = entityType.entity,
                    type = type,
                    type_id = typeId,
                    entity_count = getCount(),
                ),
            )
        }
    }

    fun insertAllRemote(collections: List<CollectionMusicBrainzNetworkModel>) {
        transacter.transaction {
            collections.forEach { collection ->
                insertRemote(collection)
            }
        }
    }

    fun getCollection(id: String): CollectionListItemModel? =
        transacter.getCollection(
            id = id,
            mapper = ::mapToCollectionListItem,
        ).executeAsOneOrNull()

    private fun getCountOfCollectionsQuery(
        showLocal: Boolean,
        showRemote: Boolean,
        query: String,
        entity: MusicBrainzEntity?,
    ): Query<Long> = transacter.getCountOfCollections(
        showLocal = showLocal,
        showRemote = showRemote,
        query = query,
        entity = entity,
    )

    fun getCountOfRemoteCollections() =
        getCountOfCollectionsQuery(
            showLocal = false,
            showRemote = true,
            query = "%%",
            entity = null,
        )
            .executeAsOne()
            .toInt()

    fun observeCountOfLocalCollections(): Flow<Int> =
        getCountOfCollectionsQuery(
            showLocal = true,
            showRemote = false,
            query = "%%",
            entity = null,
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getAllCollections(
        entity: MusicBrainzEntity?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
        entityIdToCheckExists: String?,
    ): PagingSource<Int, CollectionListItemModel> = QueryPagingSource(
        countQuery = getCountOfCollectionsQuery(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            entity = entity,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllCollections(
                entityIdToCheckExists = entityIdToCheckExists.orEmpty(),
                entity = entity,
                query = query,
                showLocal = showLocal,
                showRemote = showRemote,
                alphabetically = sortOption == CollectionSortOption.ALPHABETICALLY,
                alphabeticallyReverse = sortOption == CollectionSortOption.ALPHABETICALLY_REVERSE,
                mostEntities = sortOption == CollectionSortOption.MOST_ENTITY_COUNT,
                leastEntities = sortOption == CollectionSortOption.LEAST_ENTITY_COUNT,
                limit = limit,
                offset = offset,
                mapper = ::mapToCollectionListItem,
            )
        },
    )

    private fun mapToCollectionListItem(
        id: String,
        isRemote: Boolean,
        name: String,
        entity: MusicBrainzEntity,
        entityCount: Long,
        visited: Boolean?,
    ) = mapToCollectionListItem(
        id = id,
        isRemote = isRemote,
        name = name,
        entity = entity,
        entityCount = entityCount,
        visited = visited,
        containsEntity = null,
    )

    private fun mapToCollectionListItem(
        id: String,
        isRemote: Boolean,
        name: String,
        entity: MusicBrainzEntity,
        entityCount: Long,
        visited: Boolean?,
        containsEntity: Boolean?,
    ) = CollectionListItemModel(
        id = id,
        isRemote = isRemote,
        name = name,
        entity = entity,
        cachedEntityCount = entityCount.toInt(),
        visited = visited == true,
        containsEntity = containsEntity == true,
    )

    fun deleteMusicBrainzCollections() {
        transacter.deleteMusicBrainzCollections()
    }
}
