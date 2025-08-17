package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class ArtistCollaborationDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artist_collaborationQueries

    fun getAllCollaboratingArtistsAndEntities(
        artistId: String,
        collaborationEntityType: MusicBrainzEntityType,
        query: String,
    ): List<CollaboratingArtistAndEntity> {
        return when (collaborationEntityType) {
            MusicBrainzEntityType.RECORDING -> getAllCollaboratingArtistsAndRecordings(
                artistId = artistId,
                query = query,
            )

            MusicBrainzEntityType.RELEASE -> getAllCollaboratingArtistsAndReleases(
                artistId = artistId,
                query = query,
            )

            MusicBrainzEntityType.RELEASE_GROUP -> getAllCollaboratingArtistsAndReleaseGroups(
                artistId = artistId,
                query = query,
            )

            else -> error("Unsupported entity type: $collaborationEntityType")
        }
    }

    private fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndEntity> {
        return transacter.getAllCollaboratingArtistsAndRecordings(
            artistId = artistId,
            query = "%$query%",
            mapper = {
                    collaboratingArtistId: String,
                    collaboratingArtistName: String,
                    entityId: String,
                    entityName: String,
                ->
                CollaboratingArtistAndEntity(
                    artistId = collaboratingArtistId,
                    artistName = collaboratingArtistName,
                    entityId = entityId,
                    entityName = entityName,
                    entity = MusicBrainzEntityType.RECORDING,
                )
            },
        ).executeAsList()
    }

    private fun getAllCollaboratingArtistsAndReleases(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndEntity> {
        return transacter.getAllCollaboratingArtistsAndReleases(
            artistId = artistId,
            query = "%$query%",
            mapper = {
                    collaboratingArtistId: String,
                    collaboratingArtistName: String,
                    entityId: String,
                    entityName: String,
                ->
                CollaboratingArtistAndEntity(
                    artistId = collaboratingArtistId,
                    artistName = collaboratingArtistName,
                    entityId = entityId,
                    entityName = entityName,
                    entity = MusicBrainzEntityType.RELEASE,
                )
            },
        ).executeAsList()
    }

    private fun getAllCollaboratingArtistsAndReleaseGroups(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndEntity> {
        return transacter.getAllCollaboratingArtistsAndReleaseGroups(
            artistId = artistId,
            query = "%$query%",
            mapper = {
                    collaboratingArtistId: String,
                    collaboratingArtistName: String,
                    entityId: String,
                    entityName: String,
                ->
                CollaboratingArtistAndEntity(
                    artistId = collaboratingArtistId,
                    artistName = collaboratingArtistName,
                    entityId = entityId,
                    entityName = entityName,
                    entity = MusicBrainzEntityType.RELEASE_GROUP,
                )
            },
        ).executeAsList()
    }
}
