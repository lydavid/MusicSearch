package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.data.database.Database

class ArtistCollaborationDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artist_collaborationQueries

    fun getAllCollaboratingArtists(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording> {
        return transacter.getAllCollaboratingArtistsAndRecordings(
            artistId = artistId,
            query = "%$query%",
            mapper = {
                    collaboratingArtistId: String,
                    collaboratingArtistName: String,
                    recordingId: String,
                    recordingName: String,
                ->
                CollaboratingArtistAndRecording(
                    artistId = collaboratingArtistId,
                    artistName = collaboratingArtistName,
                    recordingId = recordingId,
                    recordingName = recordingName,
                )
            },
        ).executeAsList()
    }
}
