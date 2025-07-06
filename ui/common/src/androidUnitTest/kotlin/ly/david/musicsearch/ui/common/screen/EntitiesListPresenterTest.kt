package ly.david.musicsearch.ui.common.screen

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.list.EntitiesListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenterImpl
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EntitiesListPresenterTest {

    private fun createPresenter(
        listItems: List<ListItemModel>,
    ) = EntitiesListPresenter(
        areasListPresenter = AreasListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            areasListRepository = object : AreasListRepository {
                override fun observeAreas(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<AreaListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfAreas(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        artistsListPresenter = ArtistsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            artistsListRepository = object : ArtistsListRepository {
                override fun observeArtists(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<ArtistListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfArtists(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        eventsListPresenter = EventsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            eventsListRepository = object : EventsListRepository {
                override fun observeEvents(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<EventListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfEvents(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        genresListPresenter = GenresListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            genresListRepository = object : GenresListRepository {
                override fun observeGenres(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<GenreListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfGenres(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        instrumentsListPresenter = InstrumentsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            instrumentsListRepository = object : InstrumentsListRepository {
                override fun observeInstruments(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<InstrumentListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfInstruments(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        labelsListPresenter = LabelsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            labelsListRepository = object : LabelsListRepository {
                override fun observeLabels(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<LabelListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfLabels(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        placesListPresenter = PlacesListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            placesListRepository = object : PlacesListRepository {
                override fun observePlaces(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<PlaceListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfPlaces(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        recordingsListPresenter = RecordingsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            recordingsListRepository = object : RecordingsListRepository {
                override fun observeRecordings(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<RecordingListItemModel>> {
                    error("not used")
                }

                override fun observeCountOfRecordings(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        releasesListPresenter = ReleasesListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            releasesListRepository = object : ReleasesListRepository {
                override fun observeCountOfReleases(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }

                override fun observeReleases(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<ReleaseListItemModel>> {
                    error("Not used")
                }
            },
            appPreferences = NoOpAppPreferences(),
            musicBrainzImageMetadataRepository = NoOpMusicBrainzImageMetadataRepository(),
        ),
        releaseGroupsListPresenter = ReleaseGroupsListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            releaseGroupsListRepository = object : ReleaseGroupsListRepository {
                override fun observeCountOfEachAlbumType(
                    entityId: String,
                    isCollection: Boolean,
                ): Flow<List<ReleaseGroupTypeCount>> {
                    error("Not used")
                }

                override fun observeCountOfReleaseGroups(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }

                override fun observeReleaseGroups(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    error("Not used")
                }
            },
            appPreferences = NoOpAppPreferences(),
            musicBrainzImageMetadataRepository = NoOpMusicBrainzImageMetadataRepository(),
        ),
        seriesListPresenter = SeriesListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            seriesListRepository = object : SeriesListRepository {
                override fun observeSeries(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<SeriesListItemModel>> {
                    error("Not used")
                }

                override fun observeCountOfSeries(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        worksListPresenter = WorksListPresenter(
            getEntities = object : GetEntities {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            worksListRepository = object : WorksListRepository {
                override fun observeWorks(
                    browseMethod: BrowseMethod,
                    listFilters: ListFilters,
                ): Flow<PagingData<WorkListItemModel>> {
                    error("Not used")
                }

                override fun observeCountOfWorks(browseMethod: BrowseMethod?): Flow<Int> {
                    return flowOf(listItems.size)
                }
            },
        ),
        relationsPresenter = RelationsPresenterImpl(
            getEntityRelationships = object : GetEntityRelationships {
                override fun invoke(
                    entityId: String,
                    entity: MusicBrainzEntity?,
                    relatedEntities: Set<MusicBrainzEntity>,
                    query: String,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        tracksByReleasePresenter = TracksByReleasePresenter(
            getTracksByRelease = object : GetTracksByRelease {
                override fun invoke(
                    releaseId: String,
                    query: String,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
    )

    @Test
    fun `parameters are passed through`() = runTest {
        val presenter = createPresenter(
            listItems = listOf(
                AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
            ),
        )

        presenterTestOf({ presenter.present() }) {
            // TODO: can't sink event
            awaitItem().run {
                assertEquals(
                    listOf(
                        AreaListItemModel(
                            id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                            name = "United Kingdom",
                            countryCodes = listOf("GB"),
                        ),
                    ),
                    areasListUiState.pagingDataFlow.asSnapshot(),
                )
                assertEquals(
                    0,
                    areasListUiState.count,
                )
            }
            awaitItem().run {
                assertEquals(
                    listOf(
                        AreaListItemModel(
                            id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                            name = "United Kingdom",
                            countryCodes = listOf("GB"),
                        ),
                    ),
                    areasListUiState.pagingDataFlow.asSnapshot(),
                )
                assertEquals(
                    1,
                    areasListUiState.count,
                )
            }
        }
    }
}
