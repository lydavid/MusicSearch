package ly.david.musicsearch.data.database.dao

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Browse_entity_count

class BrowseEntityCountDao(
    database: Database,
) {
    private val transacter = database.browse_entity_countQueries

    fun insert(browseEntityCount: Browse_entity_count) {
        transacter.insert(browseEntityCount)
    }

    fun getBrowseEntityCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): Browse_entity_count? =
        transacter.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = browseEntity,
        ).executeAsOneOrNull()

    private fun updateLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        localCount: Int,
    ) {
        transacter.updateLocalCountForEntity(
            entityId = entityId,
            browseEntity = browseEntity,
            localCount = localCount,
        )
    }

    fun incrementLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        additionalOffset: Int,
    ) {
        transacter.transaction {
            val currentOffset = getBrowseEntityCount(entityId, browseEntity)?.local_count ?: 0
            updateLocalCountForEntity(entityId, browseEntity, currentOffset + additionalOffset)
        }
    }

    fun deleteBrowseEntityCountByEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ) {
        transacter.deleteBrowseEntityCountByEntity(
            entityId = entityId,
            browseEntity = browseEntity,
        )
    }

    fun deleteAllBrowseEntityCountByRemoteCollections() {
        transacter.deleteAllBrowseEntityCountByRemoteCollections()
    }
}
