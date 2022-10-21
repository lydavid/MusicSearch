package ly.david.mbjc.data.network

import ly.david.data.AreaType
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.Direction
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.persistence.history.LookupHistory

internal val countryAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "2",
    name = "Country Name",
    type = AreaType.COUNTRY
)

internal val areaAreaRelationship = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzResource.AREA,
    area = countryAreaMusicBrainzModel
)

internal val areaMusicBrainzModel = AreaMusicBrainzModel(
    id = "1",
    name = "Area Name",
    relations = listOf(
        areaAreaRelationship
    )
)

internal val artistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "1",
    name = "Artist Name",
)

internal val eventMusicBrainzModel = EventMusicBrainzModel(
    id = "1",
    name = "Event Name"
)

internal val labelMusicBrainzResource = LabelMusicBrainzModel(
    id = "1",
    name = "Label Name"
)

internal val instrumentMusicBrainzModel = InstrumentMusicBrainzModel(
    id = "1",
    name = "Instrument Name",
)

internal val placeMusicBrainzModel = PlaceMusicBrainzModel(
    id = "1",
    name = "Place Name",
)

internal val recordingMusicBrainzModel = RecordingMusicBrainzModel(
    id = "1",
    name = "Recording Name",
)

internal val releaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "1",
    name = "Release Name",
)

internal val releaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzModel(
    id = "1",
    name = "Release Group Name",
)

internal val workGroupMusicBrainzModel = WorkMusicBrainzModel(
    id = "1",
    name = "Work Name",
)

internal val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(releaseGroupMusicBrainzModel)
)

internal val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    releases = listOf(releaseMusicBrainzModel)
)

internal val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    recordings = listOf(recordingMusicBrainzModel)
)

internal val lookupHistory = LookupHistory(
    title = "欠けた心象、世のよすが",
    resource = MusicBrainzResource.RELEASE_GROUP,
    mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
    numberOfVisits = 9999
)
