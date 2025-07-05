package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod.ByEntity
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal class StatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val areasListRepository: AreasListRepository,
    private val artistsListRepository: ArtistsListRepository,
    private val eventsListRepository: EventsListRepository,
    private val genresListRepository: GenresListRepository,
    private val instrumentsListRepository: InstrumentsListRepository,
    private val labelsListRepository: LabelsListRepository,
    private val placesListRepository: PlacesListRepository,
    private val releasesListRepository: ReleasesListRepository,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val recordingsListRepository: RecordingsListRepository,
    private val worksListRepository: WorksListRepository,
    private val seriesListRepository: SeriesListRepository,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(screen.id).collectAsState(listOf())

        val tabToStats = screen.tabs
            .filterNot { setOf(Tab.DETAILS, Tab.TRACKS, Tab.STATS).contains(it) }
            .associateWith { tab ->
                val entity = tab.toMusicBrainzEntity() ?: return@associateWith EntityStats()
                observeEntityStats(
                    entityId = screen.id,
                    entity = entity,
                    localCountFlow = { entityId ->
                        observeLocalCount(
                            entity = entity,
                            entityId = entityId,
                            isCollection = screen.isCollection,
                        )
                    },
                    countOfEachAlbumTypeFlow = { entityId ->
                        if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                            observeCountOfEachAlbumType(
                                entityId = entityId,
                                isCollection = screen.isCollection,
                            )
                        } else {
                            flowOf(listOf())
                        }
                    },
                ).collectAsState(EntityStats()).value
            }.toPersistentHashMap()

        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            tabToStats = tabToStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs,
        )
    }

    private fun observeLocalCount(
        entity: MusicBrainzEntity,
        entityId: String,
        isCollection: Boolean,
    ): Flow<Int> {
        // TODO: this is an incorrect usage of ByEntity
        //  but worked for non-collection entity because there was a single count query for each
        //  and the ByEntity.entity did not matter
        val browseMethod = ByEntity(
            entity = if (isCollection) MusicBrainzEntity.COLLECTION else entity,
            entityId = entityId,
        )
        return when (entity) {
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

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeCountOfReleaseGroups(
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

    private fun observeCountOfEachAlbumType(
        entityId: String,
        isCollection: Boolean,
    ): Flow<List<ReleaseGroupTypeCount>> {
        return releaseGroupsListRepository.observeCountOfEachAlbumType(
            entityId = entityId,
            isCollection = isCollection,
        )
    }

    private fun observeEntityStats(
        entityId: String,
        entity: MusicBrainzEntity,
        localCountFlow: (entityId: String) -> Flow<Int>,
        countOfEachAlbumTypeFlow: (entityId: String) -> Flow<List<ReleaseGroupTypeCount>>,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow = browseRemoteMetadataRepository.observe(
            entityId = entityId,
            entity = entity,
        )
        return combine(
            browseRemoteMetadataFlow,
            localCountFlow(entityId),
            countOfEachAlbumTypeFlow(entityId),
        ) { browseRemoteMetadata, localCount, releaseGroupTypeCount ->
            EntityStats(
                // after adding to a remote collection, the local count may be higher than the remote count
                totalRemote = maxOf(localCount, browseRemoteMetadata?.remoteCount ?: 0),
                totalLocal = localCount,
                releaseGroupTypeCounts = releaseGroupTypeCount.map {
                    ReleaseGroupTypeCount(
                        primaryType = it.primaryType,
                        secondaryTypes = it.secondaryTypes,
                        count = it.count,
                    )
                }.toImmutableList(),
                lastUpdated = browseRemoteMetadata?.lastUpdated,
            )
        }
    }
}
