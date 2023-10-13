package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.core.artist.getDisplayNames
import ly.david.musicsearch.data.core.listitem.ReleaseGroupListItemModel

fun ReleaseGroupMusicBrainzModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        formattedArtistCredits = artistCredits.getDisplayNames(),
    )
}
