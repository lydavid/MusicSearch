package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel

internal fun mapToTrackListItemModel(
    id: String,
    mediumId: Long,
    recordingId: String,
    position: Int,
    number: String,
    title: String,
    length: Int?,
    formattedArtistCreditNames: String,
    visited: Boolean?,
) = TrackListItemModel(
    id = id,
    position = position,
    number = number,
    title = title,
    length = length,
    mediumId = mediumId,
    recordingId = recordingId,
    formattedArtistCredits = formattedArtistCreditNames,
    visited = visited == true,
)
