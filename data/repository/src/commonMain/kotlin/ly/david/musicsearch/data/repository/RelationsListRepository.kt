package ly.david.musicsearch.data.repository

import ly.david.musicsearch.data.core.listitem.RelationWithOrder

// TODO: use a different name than repository
interface RelationsListRepository {
    suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationWithOrder>?
}
