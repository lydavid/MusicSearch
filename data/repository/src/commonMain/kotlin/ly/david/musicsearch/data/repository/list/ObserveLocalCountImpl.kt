package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ObserveLocalCountImpl(
    private val areaDao: AreaDao,
    private val artistDao: ArtistDao,
    private val eventDao: EventDao,
    private val genreDao: GenreDao,
    private val instrumentDao: InstrumentDao,
    private val labelDao: LabelDao,
    private val placeDao: PlaceDao,
    private val recordingDao: RecordingDao,
    private val releaseDao: ReleaseDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val seriesDao: SeriesDao,
    private val workDao: WorkDao,
) : ObserveLocalCount {
    override fun invoke(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        if (browseMethod == null) return flowOf(0)
        return when (browseEntity) {
            MusicBrainzEntity.AREA -> areaDao.observeCountOfAreas(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.ARTIST -> artistDao.observeCountOfArtists(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.EVENT -> eventDao.observeCountOfEvents(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.GENRE -> genreDao.observeCountOfGenres(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.INSTRUMENT -> instrumentDao.observeCountOfInstruments(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.LABEL -> labelDao.observeCountOfLabels(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.PLACE -> placeDao.observeCountOfPlaces(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RECORDING -> recordingDao.observeCountOfRecordings(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RELEASE -> releaseDao.observeCountOfReleases(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupDao.observeLocalCount(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.WORK -> workDao.observeCountOfWorks(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.SERIES -> seriesDao.observeCountOfSeries(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.URL,
            -> flowOf(0)
        }
    }
}
