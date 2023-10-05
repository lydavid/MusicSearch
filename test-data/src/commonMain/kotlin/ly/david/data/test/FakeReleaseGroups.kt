package ly.david.data.test

import ly.david.data.musicbrainz.Direction
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.data.musicbrainz.api.SearchReleaseGroupsResponse

val hotSpaceReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "3918b90b-340e-3779-9d7e-ba1593653498",
    name = "Hot Space",
    firstReleaseDate = "1982-05-21"
)

val underPressureReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
    name = "Under Pressure",
    artistCredits = listOf(davidBowieArtistCredit, queenArtistCredit),
    primaryType = "Single",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "single from",
            typeId = "fcf680a9-6871-4519-8c4b-8c6549575b35",
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.RELEASE_GROUP,
            releaseGroup = hotSpaceReleaseGroup
        )
    )
)

val fakeReleaseGroupWithArtistCredits = ReleaseGroupMusicBrainzModel(
    id = "fakeReleaseGroup2",
    name = "Release Group With Artist Credits",
    artistCredits = listOf(davidBowieArtistCredit, queenArtistCredit)
)

val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(underPressureReleaseGroup)
)

val fakeReleaseGroups = listOf(
    underPressureReleaseGroup
)

val searchReleaseGroupsResponse = SearchReleaseGroupsResponse(
    count = 1,
    offset = 0,
    listOf(element = underPressureReleaseGroup)
)
