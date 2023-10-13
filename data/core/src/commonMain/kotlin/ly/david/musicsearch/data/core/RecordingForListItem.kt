package ly.david.musicsearch.data.core

data class RecordingForListItem(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String?,
    override val disambiguation: String,
    override val length: Int?,
    override val video: Boolean?,
    val isrcs: List<String>? = null,
    val formattedArtistCreditNames: String,
) : Recording
