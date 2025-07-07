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
import ly.david.musicsearch.shared.domain.BrowseMethod
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
        val byEntity = BrowseMethod.ByEntity(
            entityId = screen.byEntityId,
            entity = screen.byEntity,
        )
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(byEntity.entityId).collectAsState(listOf())

        val tabToStats = screen.tabs
            .filterNot { setOf(Tab.DETAILS, Tab.TRACKS, Tab.STATS).contains(it) }
            .associateWith { tab ->
                val browseEntity = tab.toMusicBrainzEntity() ?: return@associateWith EntityStats()
                observeEntityStats(
                    byEntity = byEntity,
                    entity = browseEntity,
                    localCountFlow = {
                        observeLocalCount(
                            browseEntity = browseEntity,
                            byEntity = byEntity,
                        )
                    },
                    countOfEachAlbumTypeFlow = { entityId ->
                        if (browseEntity == MusicBrainzEntity.RELEASE_GROUP) {
                            observeCountOfEachAlbumType(
                                entityId = entityId,
                                isCollection = byEntity.entity == MusicBrainzEntity.COLLECTION,
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
        browseEntity: MusicBrainzEntity,
        byEntity: BrowseMethod.ByEntity,
    ): Flow<Int> {
        return when (browseEntity) {
            MusicBrainzEntity.AREA -> areasListRepository.observeCountOfAreas(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.ARTIST -> artistsListRepository.observeCountOfArtists(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.EVENT -> eventsListRepository.observeCountOfEvents(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.GENRE -> genresListRepository.observeCountOfGenres(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.INSTRUMENT -> instrumentsListRepository.observeCountOfInstruments(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.LABEL -> labelsListRepository.observeCountOfLabels(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.PLACE -> placesListRepository.observeCountOfPlaces(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.RECORDING -> recordingsListRepository.observeCountOfRecordings(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.RELEASE -> releasesListRepository.observeCountOfReleases(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeCountOfReleaseGroups(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.WORK -> worksListRepository.observeCountOfWorks(
                browseMethod = byEntity,
            )

            MusicBrainzEntity.SERIES -> seriesListRepository.observeCountOfSeries(
                browseMethod = byEntity,
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
        entity: MusicBrainzEntity,
        byEntity: BrowseMethod.ByEntity,
        localCountFlow: () -> Flow<Int>,
        countOfEachAlbumTypeFlow: (entityId: String) -> Flow<List<ReleaseGroupTypeCount>>,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow = browseRemoteMetadataRepository.observe(
            entityId = byEntity.entityId,
            entity = entity,
        )
        return combine(
            browseRemoteMetadataFlow,
            localCountFlow(),
            countOfEachAlbumTypeFlow(byEntity.entityId),
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
