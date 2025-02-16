package ly.david.musicsearch.shared.feature.history

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.history.usecase.DeleteLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.GetPagedHistory
import ly.david.musicsearch.shared.domain.history.usecase.MarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.history.usecase.UnMarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.history.internal.HistoryPresenter
import ly.david.musicsearch.shared.feature.history.internal.HistoryUiEvent
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.HistoryScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * There's no need to repeat the repository tests in this layer. We should only test UI event sinking in this layer.
 */
@RunWith(RobolectricTestRunner::class)
class HistoryPresenterTest {

    private val navigator = FakeNavigator(
        root = HistoryScreen,
    )
    private val currentTime = Clock.System.now()

    private fun createHistoryPresenter(
        listItems: List<ListItemModel>,
    ) = HistoryPresenter(
        navigator = navigator,
        appPreferences = object : AppPreferences {
            override val theme: Flow<AppPreferences.Theme>
                get() = flowOf(AppPreferences.Theme.SYSTEM)

            override fun setTheme(theme: AppPreferences.Theme) {
                // No-op.
            }

            override val useMaterialYou: Flow<Boolean>
                get() = flowOf(true)

            override fun setUseMaterialYou(use: Boolean) {
                // No-op.
            }

            override val showMoreInfoInReleaseListItem: Flow<Boolean>
                get() = flowOf(true)

            override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
                // No-op.
            }

            override val sortReleaseGroupListItems: Flow<Boolean>
                get() = flowOf(true)

            override fun setSortReleaseGroupListItems(show: Boolean) {
                // No-op.
            }

            override val showLocalCollections: Flow<Boolean>
                get() = flowOf(true)

            override fun setShowLocalCollections(show: Boolean) {
                // No-op.
            }

            override val showRemoteCollections: Flow<Boolean>
                get() = flowOf(true)

            override fun setShowRemoteCollections(show: Boolean) {
                // No-op.
            }

            override val historySortOption: Flow<HistorySortOption>
                get() = flow { emit(HistorySortOption.RECENTLY_VISITED) }

            override fun setHistorySortOption(sort: HistorySortOption) {
                // No-op.
            }

            override val collectionSortOption: Flow<CollectionSortOption>
                get() = flowOf(CollectionSortOption.ALPHABETICALLY)

            override fun setCollectionSortOption(sort: CollectionSortOption) {
                // No-op.
            }

            override val coverArtsSortOption: Flow<CoverArtsSortOption>
                get() = error("Not used")

            override fun setCoverArtsSortOption(sort: CoverArtsSortOption) {
                // No-op.
            }
        },
        getPagedHistory = object : GetPagedHistory {
            override fun invoke(
                query: String,
                sortOption: HistorySortOption,
            ): Flow<PagingData<ListItemModel>> {
                return flowOf(PagingData.from(listItems))
            }
        },
        markLookupHistoryForDeletion = object : MarkLookupHistoryForDeletion {
            override fun invoke(mbid: String) {
                // No-op.
            }
        },
        unMarkLookupHistoryForDeletion = object : UnMarkLookupHistoryForDeletion {
            override fun invoke(mbid: String) {
                // No-op.
            }
        },
        deleteLookupHistory = object : DeleteLookupHistory {
            override fun invoke(mbid: String) {
                // No-op.
            }
        },
    )

    @Test
    fun `click non-collection record`() = runTest {
        val historyPresenter = createHistoryPresenter(
            listItems = listOf(
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 9999,
                    lastAccessed = currentTime,
                ),
            ),
        )

        presenterTestOf({ historyPresenter.present() }) {
            var state = awaitItem()
            assertEquals(HistorySortOption.RECENTLY_VISITED, state.sortOption)
            assertEquals("", state.topAppBarFilterState.filterText)
            assertEquals(
                listOf(
                    LookupHistoryListItemModel(
                        title = "欠けた心象、世のよすが",
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                        id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                        numberOfVisits = 9999,
                        lastAccessed = currentTime,
                    ),
                ),
                state.pagingDataFlow.asSnapshot(),
            )

            // Consider testing in isolation?
            // This doesn't affect our Presenter at all because we faked out all dependencies
            state.topAppBarFilterState.updateFilterText("hello")
            state = awaitItem()
            assertEquals("hello", state.topAppBarFilterState.filterText)

            state.eventSink(
                HistoryUiEvent.ClickItem(
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    title = "欠けた心象、世のよすが",
                ),
            )
            assertEquals(
                navigator.awaitNextScreen(),
                DetailsScreen(
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    title = "欠けた心象、世のよすが",
                ),
            )
        }
    }

    @Test
    fun `click collection record`() = runTest {
        val historyPresenter = createHistoryPresenter(
            listItems = listOf(
                LookupHistoryListItemModel(
                    title = "My collection",
                    entity = MusicBrainzEntity.COLLECTION,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3757",
                    lastAccessed = currentTime,
                ),
            ),
        )

        presenterTestOf({ historyPresenter.present() }) {
            val state = awaitItem()
            assertEquals(
                listOf(
                    LookupHistoryListItemModel(
                        title = "My collection",
                        entity = MusicBrainzEntity.COLLECTION,
                        id = "81d75493-78b6-4a37-b5ae-2a3918ee3757",
                        lastAccessed = currentTime,
                    ),
                ),
                state.pagingDataFlow.asSnapshot(),
            )

            state.eventSink(
                HistoryUiEvent.ClickItem(
                    entity = MusicBrainzEntity.COLLECTION,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3757",
                    title = "My collection",
                ),
            )
            assertEquals(
                navigator.awaitNextScreen(),
                CollectionScreen(
                    collectionId = "81d75493-78b6-4a37-b5ae-2a3918ee3757",
                ),
            )
        }
    }
}
