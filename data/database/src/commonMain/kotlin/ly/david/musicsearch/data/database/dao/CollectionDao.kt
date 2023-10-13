package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.CollectionMusicBrainzModel
import ly.david.data.musicbrainz.getCount
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Collection

class CollectionDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.collectionQueries

    fun insertLocal(collection: Collection) {
        collection.run {
            transacter.insert(
                collection = collection,
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
                    entity_count = getCount()
                )
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

    fun getCollection(id: String): Collection =
        transacter.getCollection(id).executeAsOne()

    fun getAllCollections(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String = "%%",
        entity: MusicBrainzEntity? = null,
    ): PagingSource<Int, Collection> = QueryPagingSource(
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
        )
    }

    fun deleteMusicBrainzCollections() {
        transacter.deleteMusicBrainzCollections()
    }
}
