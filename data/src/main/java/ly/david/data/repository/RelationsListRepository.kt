package ly.david.data.repository

import ly.david.data.network.RelationMusicBrainzModel

interface RelationsListRepository {
    suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>?
}
