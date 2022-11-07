package ly.david.data.network

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
        MusicBrainzResource.AREA -> fakeArea
        MusicBrainzResource.ARTIST -> fakeArtist
        MusicBrainzResource.EVENT -> eventMusicBrainzModel
        MusicBrainzResource.GENRE -> TODO()
        MusicBrainzResource.INSTRUMENT -> instrumentMusicBrainzModel
        MusicBrainzResource.LABEL -> labelMusicBrainzModel
        MusicBrainzResource.PLACE -> placeMusicBrainzModel
        MusicBrainzResource.RECORDING -> fakeRecording
        MusicBrainzResource.RELEASE -> fakeRelease
        MusicBrainzResource.RELEASE_GROUP -> fakeReleaseGroup
        MusicBrainzResource.SERIES -> seriesMusicBrainzModel
        MusicBrainzResource.WORK -> workMusicBrainzModel
        MusicBrainzResource.URL -> TODO()
    }
// endregion

// region Browse
val browseReleaseGroupsResponse = BrowseReleaseGroupsResponse(
    count = 1,
    offset = 0,
    releaseGroups = listOf(fakeReleaseGroup)
)

val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    releases = listOf(fakeRelease)
)

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    recordings = listOf(fakeRecording)
)
// endregion

// region Search
val searchAreasResponse = SearchAreasResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeArea)
)

val searchArtistsResponse = SearchArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeArtist)
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
    listOf(element = fakeRecording)
)

val searchReleasesResponse = SearchReleasesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeRelease)
)

val searchReleaseGroupsResponse = SearchReleaseGroupsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeReleaseGroup)
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
