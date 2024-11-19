package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.release.Track

data class TrackListItemModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int? = null,
    val mediumId: Long = 0,
    val recordingId: String = "",
    val formattedArtistCredits: String? = null,
    override val visited: Boolean = false,
    val mediumPosition: Int? = null,
    val mediumName: String? = null,
    val trackCount: Int = 0,
    val format: String? = null,
) : ListItemModel(), Track, Visitable
