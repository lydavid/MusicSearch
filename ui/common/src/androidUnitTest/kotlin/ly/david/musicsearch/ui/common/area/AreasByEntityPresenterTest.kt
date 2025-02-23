package ly.david.musicsearch.ui.common.area

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.usecase.GetAreasByEntity
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AreasByEntityPresenterTest {

    private fun createPresenter(
        listItems: List<AreaListItemModel>,
    ) = AreasByEntityPresenter(
        getAreasByEntity = object : GetAreasByEntity {
            override fun invoke(
                entityId: String,
                entity: MusicBrainzEntity?,
                listFilters: ListFilters,
            ): Flow<PagingData<AreaListItemModel>> {
                return flowOf(PagingData.from(listItems))
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
            val state = awaitItem()
            assertEquals(
                listOf(
                    AreaListItemModel(
                        id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                        name = "United Kingdom",
                        countryCodes = listOf("GB"),
                    ),
                ),
                state.pagingDataFlow.asSnapshot(),
            )
        }
    }
}
