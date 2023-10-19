package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.getCount
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
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
                    entity_count = entityCount,
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
                    entity = entity,
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

    fun getCollection(id: String): CollectionListItemModel =
        transacter.getCollection(
            id,
            mapper = ::mapToCollectionListItem,
        ).executeAsOne()

    fun getAllCollections(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String = "%%",
        entity: MusicBrainzEntity? = null,
    ): PagingSource<Int, CollectionListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            entity = entity,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getAllCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            limit = limit,
            offset = offset,
            entity = entity,
            mapper = ::mapToCollectionListItem,
        )
    }

    private fun mapToCollectionListItem(
        id: String,
        isRemote: Boolean,
        name: String,
        entity: MusicBrainzEntity,
        entityCount: Int,
    ) = CollectionListItemModel(
        id = id,
        isRemote = isRemote,
        name = name,
        entity = entity,
        entityCount = entityCount,
    )

    fun deleteMusicBrainzCollections() {
        transacter.deleteMusicBrainzCollections()
    }
}
