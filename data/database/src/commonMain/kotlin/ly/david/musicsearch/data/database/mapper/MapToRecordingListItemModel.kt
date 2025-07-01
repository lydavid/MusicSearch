package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel

internal fun mapToRecordingListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String?,
    length: Int?,
    video: Boolean,
    formattedArtistCreditNames: String,
    visited: Boolean?,
    collected: Boolean?,
) = RecordingListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    length = length,
    video = video,
    formattedArtistCredits = formattedArtistCreditNames,
    visited = visited == true,
    collected = collected == true,
)
