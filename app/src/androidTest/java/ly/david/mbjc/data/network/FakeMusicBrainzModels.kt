package ly.david.mbjc.data.network

import ly.david.data.AreaType
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.Direction
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.persistence.history.LookupHistory

internal val fakeCountryAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "2",
    name = "Country Name",
    type = AreaType.COUNTRY
)

internal val fakeAreaAreaRelationship = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzResource.AREA,
    area = fakeCountryAreaMusicBrainzModel
)

internal val fakeAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "1",
    name = "Area Name",
    relations = listOf(
        fakeAreaAreaRelationship
    )
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
