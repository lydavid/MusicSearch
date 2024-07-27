package ly.david.musicsearch.core.models.artist

data class CollaboratingArtistAndRecording(
    val artistId: String,
    val artistName: String,
    val recordingId: String,
    val recordingName: String,
) {
    override fun toString(): String {
        return "CollaboratingArtistAndRecording(artistId=\"${artistId}\",artistName=\"${artistName}\"," +
            "recordingId=\"${recordingId}\",recordingName=\"${recordingName}\")"
    }
}
