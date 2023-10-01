package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database

class EntityHasUrlsDao(
    private val database: Database,
) {
    fun markEntityHasUrls(entityId: String) {
        database.mb_entity_has_urlsQueries.insert(
            entity_id = entityId,
            has_urls = true,
        )
    }

    fun hasUrls(entityId: String): Boolean {
        return database.mb_entity_has_urlsQueries.hasUrls(entityId = entityId)
            .executeAsOneOrNull()?.has_urls == true
    }
}
