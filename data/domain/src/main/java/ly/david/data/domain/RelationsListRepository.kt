package ly.david.data.domain

import ly.david.data.network.RelationMusicBrainzModel

// TODO: we shouldn't use inheritance for this
interface RelationsListRepository {
    suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>?
}
