package ly.david.musicsearch.data.database.mapper

import ly.david.data.core.RecordingForListItem

internal fun mapToRecordingForListItem(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String?,
    length: Int?,
    video: Boolean,
    isrcs: List<String>?,
    formattedArtistCreditNames: String,
) = RecordingForListItem(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    length = length,
    isrcs = isrcs,
    video = video,
    formattedArtistCreditNames = formattedArtistCreditNames,
)
