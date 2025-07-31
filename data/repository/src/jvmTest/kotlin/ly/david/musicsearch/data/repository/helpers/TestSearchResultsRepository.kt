package ly.david.musicsearch.data.repository.helpers

import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.repository.search.SearchResultsRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

interface TestSearchResultsRepository {

    val searchResultDao: SearchResultDao
    val areaDao: AreaDao
    val artistDao: ArtistDao
    val eventDao: EventDao
    val instrumentDao: InstrumentDao
    val labelDao: LabelDao
    val placeDao: PlaceDao
    val recordingDao: RecordingDao
    val releaseDao: ReleaseDao
    val releaseGroupDao: ReleaseGroupDao
    val artistCreditDao: ArtistCreditDao
    val seriesDao: SeriesDao
    val workDao: WorkDao
    val aliasDao: AliasDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createSearchResultsRepository(
        searchApi: SearchApi,
    ): SearchResultsRepository {
        return SearchResultsRepositoryImpl(
            searchApi = searchApi,
            searchResultDao = searchResultDao,
            areaDao = areaDao,
            artistDao = artistDao,
            eventDao = eventDao,
            instrumentDao = instrumentDao,
            labelDao = labelDao,
            placeDao = placeDao,
            recordingDao = recordingDao,
            releaseDao = releaseDao,
            releaseGroupDao = releaseGroupDao,
            seriesDao = seriesDao,
            workDao = workDao,
            aliasDao = aliasDao,
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}
