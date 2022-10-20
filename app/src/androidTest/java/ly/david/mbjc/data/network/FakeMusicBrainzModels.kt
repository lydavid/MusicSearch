package ly.david.mbjc.data.network

import ly.david.mbjc.data.network.api.BrowseReleaseGroupsResponse
import ly.david.mbjc.data.persistence.history.LookupHistory

internal val areaMusicBrainzModel = AreaMusicBrainzModel(
    id = "1",
    name = "Area Name"
)

internal val artistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "1",
    name = "Artist Name",
)

internal val releaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzModel(
    id = "1",
    name = "Release Group Name",
)

internal val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(releaseGroupMusicBrainzModel)
)

internal val lookupHistory = LookupHistory(
    title = "欠けた心象、世のよすが",
    resource = MusicBrainzResource.RELEASE_GROUP,
    mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
    numberOfVisits = 9999
)
