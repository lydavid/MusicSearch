package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ArtistCollaborationDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artist_collaborationQueries

    fun getAllCollaboratingArtistsAndEntities(
        artistId: String,
        collaborationEntityType: MusicBrainzEntity,
        query: String,
    ): List<CollaboratingArtistAndEntity> {
        return when (collaborationEntityType) {
            MusicBrainzEntity.RECORDING -> getAllCollaboratingArtistsAndRecordings(
                artistId = artistId,
                query = query,
            )

            MusicBrainzEntity.RELEASE -> getAllCollaboratingArtistsAndReleases(
                artistId = artistId,
                query = query,
            )

            MusicBrainzEntity.RELEASE_GROUP -> getAllCollaboratingArtistsAndReleaseGroups(
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
                    entity = MusicBrainzEntity.RECORDING,
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
                    entity = MusicBrainzEntity.RELEASE,
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
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                )
            },
        ).executeAsList()
    }
}
