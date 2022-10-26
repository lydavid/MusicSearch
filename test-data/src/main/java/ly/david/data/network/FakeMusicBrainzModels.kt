package ly.david.data.network

import ly.david.data.AreaType
import ly.david.data.NameWithDisambiguation
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.SearchAreasResponse
import ly.david.data.network.api.SearchArtistsResponse
import ly.david.data.network.api.SearchEventsResponse
import ly.david.data.network.api.SearchInstrumentsResponse
import ly.david.data.network.api.SearchLabelsResponse
import ly.david.data.network.api.SearchPlacesResponse
import ly.david.data.network.api.SearchRecordingsResponse
import ly.david.data.network.api.SearchReleaseGroupsResponse
import ly.david.data.network.api.SearchReleasesResponse
import ly.david.data.network.api.SearchSeriesResponse
import ly.david.data.network.api.SearchWorksResponse
import ly.david.data.persistence.history.LookupHistory

// region Lookup
val countryAreaMusicBrainzModel = AreaMusicBrainzModel(
    id = "2",
    name = "Country Name",
    type = AreaType.COUNTRY
)

val areaAreaRelationship = RelationMusicBrainzModel(
    type = "doesn't matter",
    typeId = "de7cc874-8b1b-3a05-8272-f3834c968fb7",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzResource.AREA,
    area = countryAreaMusicBrainzModel
)

val areaMusicBrainzModel = AreaMusicBrainzModel(
    id = "1",
    name = "Area Name",
    relations = listOf(
        areaAreaRelationship
    )
)

val artistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "1",
    name = "Artist Name",
)

val eventMusicBrainzModel = EventMusicBrainzModel(
    id = "1",
    name = "Event Name"
)

val labelMusicBrainzModel = LabelMusicBrainzModel(
    id = "1",
    name = "Label Name"
)

val instrumentMusicBrainzModel = InstrumentMusicBrainzModel(
    id = "1",
    name = "Instrument Name",
)

val placeMusicBrainzModel = PlaceMusicBrainzModel(
    id = "1",
    name = "Place Name",
)

val recordingMusicBrainzModel = RecordingMusicBrainzModel(
    id = "1",
    name = "Recording Name",
)

val releaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "1",
    name = "Release Name",
)

val releaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzModel(
    id = "1",
    name = "Release Group Name",
)

val seriesMusicBrainzModel = SeriesMusicBrainzModel(
    id = "1",
    name = "Series Name",
)

val workMusicBrainzModel = WorkMusicBrainzModel(
    id = "1",
    name = "Work Name",
)

fun MusicBrainzResource.toFakeMusicBrainzModel(): NameWithDisambiguation =
    when (this) {
        MusicBrainzResource.AREA -> areaMusicBrainzModel
        MusicBrainzResource.ARTIST -> artistMusicBrainzModel
        MusicBrainzResource.EVENT -> eventMusicBrainzModel
        MusicBrainzResource.GENRE -> TODO()
        MusicBrainzResource.INSTRUMENT -> instrumentMusicBrainzModel
        MusicBrainzResource.LABEL -> labelMusicBrainzModel
        MusicBrainzResource.PLACE -> placeMusicBrainzModel
        MusicBrainzResource.RECORDING -> recordingMusicBrainzModel
        MusicBrainzResource.RELEASE -> releaseMusicBrainzModel
        MusicBrainzResource.RELEASE_GROUP -> releaseGroupMusicBrainzModel
        MusicBrainzResource.SERIES -> seriesMusicBrainzModel
        MusicBrainzResource.WORK -> workMusicBrainzModel
        MusicBrainzResource.URL -> TODO()
    }
// endregion

// region Browse
val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(releaseGroupMusicBrainzModel)
)

val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    releases = listOf(releaseMusicBrainzModel)
)

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    recordings = listOf(recordingMusicBrainzModel)
)
// endregion

// region Search
val searchAreasResponse = SearchAreasResponse(
    count = 1,
    offset = 0,
    listOf(element = areaMusicBrainzModel)
)

val searchArtistsResponse = SearchArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = artistMusicBrainzModel)
)

val searchEventsResponse = SearchEventsResponse(
    count = 1,
    offset = 0,
    listOf(element = eventMusicBrainzModel)
)

val searchInstrumentsResponse = SearchInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = instrumentMusicBrainzModel)
)

val searchLabelsResponse = SearchLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = labelMusicBrainzModel)
)

val searchPlacesResponse = SearchPlacesResponse(
    count = 1,
    offset = 0,
    listOf(element = placeMusicBrainzModel)
)

val searchRecordingsResponse = SearchRecordingsResponse(
    count = 1,
    offset = 0,
    listOf(element = recordingMusicBrainzModel)
)

val searchReleasesResponse = SearchReleasesResponse(
    count = 1,
    offset = 0,
    listOf(element = releaseMusicBrainzModel)
)

val searchReleaseGroupsResponse = SearchReleaseGroupsResponse(
    count = 1,
    offset = 0,
    listOf(element = releaseGroupMusicBrainzModel)
)

val searchSeriesResponse = SearchSeriesResponse(
    count = 1,
    offset = 0,
    listOf(element = seriesMusicBrainzModel)
)

val searchWorksResponse = SearchWorksResponse(
    count = 1,
    offset = 0,
    listOf(element = workMusicBrainzModel)
)
// endregion


val lookupHistory = LookupHistory(
    title = "欠けた心象、世のよすが",
    resource = MusicBrainzResource.RELEASE_GROUP,
    mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
    numberOfVisits = 9999
)
