package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database

class EntityHasRelationsDao(
    private val database: Database,
) {
    fun markEntityHasRelationsStored(entityId: String) {
        database.mb_entity_has_relationsQueries.insert(
            entity_id = entityId,
            has_relations = true,
        )
    }

    fun hasRelationsBeenSavedFor(entityId: String): Boolean {
        return database.mb_entity_has_relationsQueries.hasRelations(entityId = entityId)
            .executeAsOneOrNull()?.has_relations == true
    }
}
