package ly.david.musicsearch.data.database.mapper

import ly.david.data.core.TrackForListItem

internal fun mapToTrackForListItem(
    id: String,
    mediumId: Long,
    recordingId: String,
    position: Int,
    number: String,
    title: String,
    length: Int?,
    formattedArtistCreditNames: String,
) = TrackForListItem(
    id = id,
    position = position,
    number = number,
    title = title,
    length = length,
    mediumId = mediumId,
    recordingId = recordingId,
    formattedArtistCreditNames = formattedArtistCreditNames,
)
