package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.musicsearch.data.core.artist.getDisplayNames
import ly.david.musicsearch.data.core.listitem.RecordingListItemModel

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
