package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.recording.Recording

data class RecordingListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val formattedArtistCredits: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Recording
