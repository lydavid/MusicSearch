package ly.david.musicsearch.ui.common.area

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.area.usecase.GetAreas
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AreasListPresenterTest {

    private fun createPresenter(
        listItems: List<ListItemModel>,
    ) = AreasListPresenter(
        getAreas = object : GetAreas {
            override fun invoke(
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
                error("Not used")
            }

            override fun observeCountOfAreas(browseMethod: BrowseMethod?): Flow<Int> {
                return flowOf(listItems.size)
            }
        },
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
