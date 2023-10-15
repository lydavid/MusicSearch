package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.recording.Recording

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
