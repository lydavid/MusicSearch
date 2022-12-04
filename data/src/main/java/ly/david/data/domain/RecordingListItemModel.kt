package ly.david.data.domain

import ly.david.data.Recording
import ly.david.data.getDisplayNames
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.persistence.recording.RecordingForListItem

data class RecordingListItemModel(
    override val id: String,
    override val name: String,
    override val date: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val formattedArtistCredits: String? = null,
) : UiModel(), Recording

// Only used for search screen where we don't commit to inserting every search result
fun RecordingMusicBrainzModel.toListItemModel() = RecordingListItemModel(
    id = id,
    name = name,
    date = date,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    formattedArtistCredits = artistCredits.getDisplayNames()
)

fun RecordingForListItem.toListItemModel() = RecordingListItemModel(
    id = recording.id,
    name = recording.name,
    date = recording.date,
    disambiguation = recording.disambiguation,
    length = recording.length,
    video = recording.video,
    formattedArtistCredits = artistCreditNames
)
