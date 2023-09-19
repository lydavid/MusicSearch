package ly.david.data.domain.listitem

import ly.david.data.core.Recording
import ly.david.data.core.RecordingWithArtistCredits
import ly.david.data.core.getDisplayNames
import ly.david.data.musicbrainz.RecordingMusicBrainzModel

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

// Only used for search screen where we don't commit to inserting every search result
fun RecordingMusicBrainzModel.toRecordingListItemModel() = RecordingListItemModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    formattedArtistCredits = artistCredits.getDisplayNames()
)

fun RecordingWithArtistCredits.toRecordingListItemModel() = RecordingListItemModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    formattedArtistCredits = name
)
