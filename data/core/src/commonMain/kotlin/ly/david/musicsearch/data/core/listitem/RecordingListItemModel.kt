package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.recording.Recording

data class RecordingListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val formattedArtistCredits: String? = null,
    // Not displaying isrcs for list items
) : ListItemModel(), Recording
