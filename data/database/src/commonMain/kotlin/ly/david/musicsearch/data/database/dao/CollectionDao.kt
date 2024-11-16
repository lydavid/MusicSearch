package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.getCount
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

    fun insertRemote(collection: CollectionMusicBrainzModel) {
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

    fun insertAllRemote(collections: List<CollectionMusicBrainzModel>) {
        transacter.transaction {
            collections.forEach { collection ->
                insertRemote(collection)
            }
        }
    }

    fun getCollection(id: String): CollectionListItemModel? =
        transacter.getCollection(
            id,
            mapper = ::mapToCollectionListItem,
        ).executeAsOneOrNull()

    fun getAllCollections(
        entity: MusicBrainzEntity?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
    ): PagingSource<Int, CollectionListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            entity = entity,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllCollections(
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
    ) = CollectionListItemModel(
        id = id,
        isRemote = isRemote,
        name = name,
        entity = entity,
        cachedEntityCount = entityCount.toInt(),
        visited = visited == true,
    )

    fun deleteMusicBrainzCollections() {
        transacter.deleteMusicBrainzCollections()
    }
}
