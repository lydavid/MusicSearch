package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.core.models.listitem.RecordingListItemModel

internal fun mapToRecordingListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    firstReleaseDate: String?,
    length: Int?,
    video: Boolean,
    formattedArtistCreditNames: String,
) = RecordingListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    firstReleaseDate = firstReleaseDate,
    length = length,
    video = video,
    formattedArtistCredits = formattedArtistCreditNames,
)
