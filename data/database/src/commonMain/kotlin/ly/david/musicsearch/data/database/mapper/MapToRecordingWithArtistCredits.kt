package ly.david.musicsearch.data.database.mapper

import ly.david.data.core.RecordingWithArtistCredits

internal fun mapToRecordingWithArtistCredits(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String?,
    length: Int?,
    video: Boolean,
    isrcs: List<String>?,
    formattedArtistCreditNames: String?,
) = RecordingWithArtistCredits(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    length = length,
    isrcs = isrcs,
    video = video,
    formattedArtistCreditNames = formattedArtistCreditNames.orEmpty()
)
