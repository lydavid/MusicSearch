package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.Track
import ly.david.musicsearch.data.core.TrackForListItem

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

fun TrackForListItem.toTrackListItemModel() =
    TrackListItemModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length,
        mediumId = mediumId,
        recordingId = recordingId,
        formattedArtistCredits = formattedArtistCreditNames,
    )
