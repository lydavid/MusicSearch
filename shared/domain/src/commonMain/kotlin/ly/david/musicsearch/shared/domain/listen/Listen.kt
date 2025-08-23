package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel

data class Listen(
    val insertedAtMs: Long,
    val listenedAtMs: Long,
    val recordingMessybrainzId: String,
    val username: String,
    val recordingMusicbrainzId: String?,
    val caaId: Long?,
    val caaReleaseMbid: String?,
    val artistName: String,
    val trackName: String,
    val releaseName: String?,
    val durationMs: Long?,
    val mediaPlayer: String?,
    val submissionClient: String?,
    val musicService: String?,
    val musicServiceName: String?,
    val originUrl: String?,
    val spotifyAlbumArtistIds: List<String>?,
    val spotifyAlbumId: String?,
    val spotifyArtistIds: List<String>?,
    val spotifyId: String?,
    val recordingName: String?,
    val artistCredits: List<ArtistCreditUiModel>,
)
