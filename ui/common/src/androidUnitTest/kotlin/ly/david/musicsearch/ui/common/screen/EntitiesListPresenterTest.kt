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
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
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

    private class FakeGetEntities(private val listItems: List<ListItemModel>) : GetEntities {
        override fun invoke(
            entity: MusicBrainzEntity,
            browseMethod: BrowseMethod?,
            listFilters: ListFilters,
        ): Flow<PagingData<ListItemModel>> {
            return flowOf(PagingData.from(listItems))
        }
    }

    private class FakeEntitiesListRepository(private val listItems: List<ListItemModel>) : EntitiesListRepository {
        override fun observeEntities(
            entity: MusicBrainzEntity,
            browseMethod: BrowseMethod,
            listFilters: ListFilters,
        ): Flow<PagingData<ListItemModel>> {
            error("not used")
        }

        override fun observeLocalCount(
            browseEntity: MusicBrainzEntity,
            browseMethod: BrowseMethod?,
        ): Flow<Int> {
            return flowOf(listItems.size)
        }
    }

    private fun createPresenter(
        listItems: List<ListItemModel>,
    ) = EntitiesListPresenter(
        areasListPresenter = AreasListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        artistsListPresenter = ArtistsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        eventsListPresenter = EventsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        genresListPresenter = GenresListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        instrumentsListPresenter = InstrumentsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        labelsListPresenter = LabelsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        placesListPresenter = PlacesListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        recordingsListPresenter = RecordingsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        releasesListPresenter = ReleasesListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
            appPreferences = NoOpAppPreferences(),
            musicBrainzImageMetadataRepository = NoOpMusicBrainzImageMetadataRepository(),
        ),
        releaseGroupsListPresenter = ReleaseGroupsListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
            appPreferences = NoOpAppPreferences(),
            musicBrainzImageMetadataRepository = NoOpMusicBrainzImageMetadataRepository(),
        ),
        seriesListPresenter = SeriesListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
        ),
        worksListPresenter = WorksListPresenter(
            getEntities = FakeGetEntities(listItems),
            entitiesListRepository = FakeEntitiesListRepository(listItems),
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
