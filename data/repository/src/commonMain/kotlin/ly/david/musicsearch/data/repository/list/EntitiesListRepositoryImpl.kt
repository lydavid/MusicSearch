package ly.david.musicsearch.data.repository.list

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository

class EntitiesListRepositoryImpl(
    private val areasListRepository: AreasListRepository,
    private val artistsListRepository: ArtistsListRepository,
    private val eventsListRepository: EventsListRepository,
    private val genresListRepository: GenresListRepository,
    private val instrumentsListRepository: InstrumentsListRepository,
    private val labelsListRepository: LabelsListRepository,
    private val placesListRepository: PlacesListRepository,
    private val recordingsListRepository: RecordingsListRepository,
    private val releasesListRepository: ReleasesListRepository,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val seriesListRepository: SeriesListRepository,
    private val worksListRepository: WorksListRepository,
) : EntitiesListRepository {

    override fun observeEntities(
        entity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        @Suppress("UNCHECKED_CAST")
        return when (entity) {
            MusicBrainzEntity.AREA -> areasListRepository.observeAreas(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.ARTIST -> artistsListRepository.observeArtists(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.EVENT -> eventsListRepository.observeEvents(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.GENRE -> genresListRepository.observeGenres(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.INSTRUMENT -> instrumentsListRepository.observeInstruments(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.LABEL -> labelsListRepository.observeLabels(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.PLACE -> placesListRepository.observePlaces(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.RECORDING -> recordingsListRepository.observeRecordings(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.RELEASE -> releasesListRepository.observeReleases(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeReleaseGroups(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )

            MusicBrainzEntity.SERIES -> seriesListRepository.observeSeries(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.WORK -> worksListRepository.observeWorks(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.URL,
            -> flowOf(PagingData.empty())
        }
    }

    override fun observeLocalCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int> {
        if (browseMethod == null) return flowOf(0)
        return when (browseEntity) {
            MusicBrainzEntity.AREA -> areasListRepository.observeCountOfAreas(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.ARTIST -> artistsListRepository.observeCountOfArtists(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.EVENT -> eventsListRepository.observeCountOfEvents(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.GENRE -> genresListRepository.observeCountOfGenres(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.INSTRUMENT -> instrumentsListRepository.observeCountOfInstruments(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.LABEL -> labelsListRepository.observeCountOfLabels(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.PLACE -> placesListRepository.observeCountOfPlaces(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RECORDING -> recordingsListRepository.observeCountOfRecordings(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RELEASE -> releasesListRepository.observeCountOfReleases(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeLocalCount(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.WORK -> worksListRepository.observeCountOfWorks(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.SERIES -> seriesListRepository.observeCountOfSeries(
                browseMethod = browseMethod,
            )

            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.URL,
            -> flowOf(0)
        }
    }

    override fun observeVisitedCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod?,
    ): Flow<Int?> {
        if (browseMethod == null) return flowOf(null)
        return when (browseEntity) {
            MusicBrainzEntity.AREA -> flowOf(null)
            MusicBrainzEntity.ARTIST -> flowOf(null)
            MusicBrainzEntity.COLLECTION -> flowOf(null)
            MusicBrainzEntity.EVENT -> flowOf(null)
            MusicBrainzEntity.GENRE -> flowOf(null)
            MusicBrainzEntity.INSTRUMENT -> flowOf(null)
            MusicBrainzEntity.LABEL -> flowOf(null)
            MusicBrainzEntity.PLACE -> flowOf(null)
            MusicBrainzEntity.RECORDING -> flowOf(null)
            MusicBrainzEntity.RELEASE -> flowOf(null)
            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeVisitedCount(browseMethod)
            MusicBrainzEntity.SERIES -> flowOf(null)
            MusicBrainzEntity.WORK -> flowOf(null)
            MusicBrainzEntity.URL -> flowOf(null)
        }
    }
}
