package ly.david.data.domain

import ly.david.data.network.RelationMusicBrainzModel

interface RelationsListRepository {
    suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>?
}
