package ly.david.data.network

import ly.david.data.NameWithDisambiguation
import ly.david.data.network.api.BrowseRecordingsResponse
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

val instrumentMusicBrainzModel = InstrumentMusicBrainzModel(
    id = "1",
    name = "Instrument Name",
)

val seriesMusicBrainzModel = SeriesMusicBrainzModel(
    id = "1",
    name = "Series Name",
)

fun MusicBrainzResource.toFakeMusicBrainzModel(): NameWithDisambiguation =
    when (this) {
        MusicBrainzResource.AREA -> fakeArea
        MusicBrainzResource.ARTIST -> fakeArtist
        MusicBrainzResource.EVENT -> fakeEvent
        MusicBrainzResource.GENRE -> TODO()
        MusicBrainzResource.INSTRUMENT -> instrumentMusicBrainzModel
        MusicBrainzResource.LABEL -> fakeLabel
        MusicBrainzResource.PLACE -> fakePlace
        MusicBrainzResource.RECORDING -> fakeRecording
        MusicBrainzResource.RELEASE -> fakeRelease
        MusicBrainzResource.RELEASE_GROUP -> fakeReleaseGroup
        MusicBrainzResource.SERIES -> seriesMusicBrainzModel
        MusicBrainzResource.WORK -> fakeWork
        MusicBrainzResource.URL -> TODO()
    }

val browseRecordingsResponse = BrowseRecordingsResponse(
    count = 1,
    offset = 0,
    recordings = listOf(fakeRecording)
)

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
    listOf(element = fakeEvent)
)

val searchInstrumentsResponse = SearchInstrumentsResponse(
    count = 1,
    offset = 0,
    listOf(element = instrumentMusicBrainzModel)
)

val searchLabelsResponse = SearchLabelsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeLabel)
)

val searchPlacesResponse = SearchPlacesResponse(
    count = 1,
    offset = 0,
    listOf(element = fakePlace)
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
    listOf(element = fakeWork)
)




val lookupHistory = LookupHistory(
    title = "欠けた心象、世のよすが",
    resource = MusicBrainzResource.RELEASE_GROUP,
    mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
    numberOfVisits = 9999
)
