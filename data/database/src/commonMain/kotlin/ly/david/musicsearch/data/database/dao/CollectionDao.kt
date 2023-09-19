package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.musicbrainz.CollectionMusicBrainzModel
import ly.david.data.musicbrainz.getCount
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Collection

class CollectionDao(
    database: Database,
) {
    private val transacter = database.collectionQueries

    fun insert(collection: CollectionMusicBrainzModel) {
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

    fun insertAll(collections: List<CollectionMusicBrainzModel>) {
        transacter.transaction {
            collections.forEach { collection ->
                insert(collection)
            }
        }
    }

    fun getCollection(id: String): Collection? =
        transacter.getCollection(id).executeAsOneOrNull()

    fun getAllCollections(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String,
    ): PagingSource<Int, Collection> = QueryPagingSource(
        countQuery = transacter.getNumberOfCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getAllCollections(
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            limit = limit,
            offset = offset,
        )
    }

    fun deleteMusicBrainzCollections() {
        transacter.deleteMusicBrainzCollections()
    }
}
