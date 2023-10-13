package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Track

data class TrackListItemModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int? = null,
    val mediumId: Long = 0,
    val recordingId: String = "",
    val formattedArtistCredits: String? = null,
) : ListItemModel(), Track
