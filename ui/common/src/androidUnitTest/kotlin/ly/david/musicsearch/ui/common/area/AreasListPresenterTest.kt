package ly.david.musicsearch.ui.common.area

import androidx.paging.testing.asSnapshot
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.screen.NoOpMusicBrainzImageMetadataRepository
import ly.david.musicsearch.ui.common.utils.FakeGetEntities
import ly.david.musicsearch.ui.common.utils.FakeObserveLocalCount
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AreasListPresenterTest {

    private fun createPresenter(
        listItems: List<ListItemModel>,
    ) = AreasListPresenter(
        getEntities = FakeGetEntities(listItems),
        observeLocalCount = FakeObserveLocalCount(listItems),
        appPreferences = NoOpAppPreferences(),
        musicBrainzImageMetadataRepository = NoOpMusicBrainzImageMetadataRepository(),
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
            awaitItem().run {
                assertEquals(
                    listOf(
                        AreaListItemModel(
                            id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                            name = "United Kingdom",
                            countryCodes = listOf("GB"),
                        ),
                    ),
                    pagingDataFlow.asSnapshot(),
                )
                assertEquals(
                    0,
                    count,
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
                    pagingDataFlow.asSnapshot(),
                )
                assertEquals(
                    1,
                    count,
                )
            }
        }
    }
}
