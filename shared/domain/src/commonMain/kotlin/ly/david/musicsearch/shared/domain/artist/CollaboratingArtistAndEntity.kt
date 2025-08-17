package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class CollaboratingArtistAndEntity(
    val artistId: String,
    val artistName: String,
    val entityId: String,
    val entityName: String,
    val entity: MusicBrainzEntityType,
) {
    override fun toString(): String {
        return "CollaboratingArtistAndRecording(artistId=\"${artistId}\",artistName=\"${artistName}\"," +
            "entityId=\"${entityId}\",entityName=\"${entityName}\",entity=\"${entity}\")"
    }
}
