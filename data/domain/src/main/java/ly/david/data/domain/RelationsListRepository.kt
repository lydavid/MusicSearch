package ly.david.data.domain

import ly.david.data.musicbrainz.RelationMusicBrainzModel

// TODO: use a different name than repository
interface RelationsListRepository {
    suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>?
}
