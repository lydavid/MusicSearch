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
import ly.david.musicsearch.shared.domain.area.usecase.GetAreas
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtists
import ly.david.musicsearch.shared.domain.event.usecase.GetEvents
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenres
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstruments
import ly.david.musicsearch.shared.domain.label.usecase.GetLabels
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.usecase.GetPlaces
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordings
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.release.usecase.GetReleases
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroups
import ly.david.musicsearch.shared.domain.series.usecase.GetSeries
import ly.david.musicsearch.shared.domain.work.usecase.GetWorks
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
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
            getAreas = object : GetAreas {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        artistsListPresenter = ArtistsListPresenter(
            getArtists = object : GetArtists {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        eventsListPresenter = EventsListPresenter(
            getEvents = object : GetEvents {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        genresListPresenter = GenresListPresenter(
            getGenres = object : GetGenres {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        instrumentsListPresenter = InstrumentsListPresenter(
            getInstruments = object : GetInstruments {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        labelsListPresenter = LabelsListPresenter(
            getLabels = object : GetLabels {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        placesListPresenter = PlacesListPresenter(
            getPlaces = object : GetPlaces {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        recordingsListPresenter = RecordingsListPresenter(
            getRecordings = object : GetRecordings {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        releasesListPresenter = ReleasesListPresenter(
            getReleases = object : GetReleases {
                override fun invoke(
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
            getReleaseGroups = object : GetReleaseGroups {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
            releaseGroupsListRepository = object : ReleaseGroupsListRepository {
                override fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>> {
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
            getSeries = object : GetSeries {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        worksListPresenter = WorksListPresenter(
            getWorks = object : GetWorks {
                override fun invoke(
                    browseMethod: BrowseMethod?,
                    listFilters: ListFilters,
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            },
        ),
        relationsPresenter = RelationsPresenterImpl(
            getEntityRelationships = object : GetEntityRelationships {
                override fun invoke(
                    entityId: String,
                    entity: MusicBrainzEntity?,
                    relatedEntities: Set<MusicBrainzEntity>,
                    query: String
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            }
        ),
        tracksByReleasePresenter = TracksByReleasePresenter(
            getTracksByRelease = object : GetTracksByRelease {
                override fun invoke(
                    releaseId: String,
                    query: String
                ): Flow<PagingData<ListItemModel>> {
                    return flowOf(PagingData.from(listItems))
                }
            }
        )
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
            val state = awaitItem()

            // TODO: can't sink event

            assertEquals(
                listOf(
                    AreaListItemModel(
                        id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                        name = "United Kingdom",
                        countryCodes = listOf("GB"),
                    ),
                ),
                state.areasListUiState.pagingDataFlow.asSnapshot(),
            )
        }
    }
}
