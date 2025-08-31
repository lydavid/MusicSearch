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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import kotlin.time.Clock

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
        entity: MusicBrainzEntityType,
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        @Suppress("UNCHECKED_CAST")
        return when (entity) {
            MusicBrainzEntityType.AREA -> areasListRepository.observeAreas(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.ARTIST -> artistsListRepository.observeArtists(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.EVENT -> eventsListRepository.observeEvents(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.GENRE -> genresListRepository.observeGenres(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.INSTRUMENT -> instrumentsListRepository.observeInstruments(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.LABEL -> labelsListRepository.observeLabels(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.PLACE -> placesListRepository.observePlaces(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.RECORDING -> recordingsListRepository.observeRecordings(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.RELEASE -> releasesListRepository.observeReleases(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.RELEASE_GROUP -> releaseGroupsListRepository.observeReleaseGroups(
                browseMethod = browseMethod,
                listFilters = listFilters,
                now = Clock.System.now(),
            )

            MusicBrainzEntityType.SERIES -> seriesListRepository.observeSeries(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.WORK -> worksListRepository.observeWorks(
                browseMethod = browseMethod,
                listFilters = listFilters,
            ) as Flow<PagingData<ListItemModel>>

            MusicBrainzEntityType.COLLECTION,
            MusicBrainzEntityType.URL,
            -> flowOf(PagingData.empty())
        }
    }
}
