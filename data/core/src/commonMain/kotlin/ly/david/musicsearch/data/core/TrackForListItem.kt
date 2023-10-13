package ly.david.musicsearch.data.core

data class TrackForListItem(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int?,
    val mediumId: Long,
    val recordingId: String,
    val formattedArtistCreditNames: String,
) : Track
