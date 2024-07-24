package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.core.models.artist.ArtistDetailsModel
import ly.david.musicsearch.core.models.artist.CollaboratingArtist
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel

    fun getAllCollaboratingArtists(artistId: String): List<CollaboratingArtist>

    fun getAllCollaboratedRecordings(artistId: String): List<RecordingListItemModel>
}
