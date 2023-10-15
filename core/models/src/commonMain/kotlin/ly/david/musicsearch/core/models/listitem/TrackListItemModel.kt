package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.release.Track

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
