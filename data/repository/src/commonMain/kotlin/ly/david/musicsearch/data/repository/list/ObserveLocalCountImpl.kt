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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

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
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        if (browseMethod == null) return flowOf(0)
        return when (browseEntity) {
            MusicBrainzEntityType.AREA -> areaDao.observeCountOfAreas(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.ARTIST -> artistDao.observeCountOfArtists(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.EVENT -> eventDao.observeCountOfEvents(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.GENRE -> genreDao.observeCountOfGenres(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.INSTRUMENT -> instrumentDao.observeCountOfInstruments(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.LABEL -> labelDao.observeCountOfLabels(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.PLACE -> placeDao.observeCountOfPlaces(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.RECORDING -> recordingDao.observeCountOfRecordings(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.RELEASE -> releaseDao.observeCountOfReleases(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.RELEASE_GROUP -> releaseGroupDao.observeLocalCount(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.WORK -> workDao.observeCountOfWorks(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.SERIES -> seriesDao.observeCountOfSeries(
                browseMethod = browseMethod,
            )

            MusicBrainzEntityType.COLLECTION,
            MusicBrainzEntityType.URL,
            -> flowOf(0)
        }
    }
}
