package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.listitem.toListItemModel
import ly.david.data.domain.paging.SearchMusicBrainzPagingSource
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.FakeMusicBrainzApiService
import ly.david.data.network.searchableResources
import ly.david.data.network.toFakeMusicBrainzModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class SearchMusicBrainzPagingSourceTest(private val resource: MusicBrainzResource) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzResource> {
            return searchableResources
        }
    }

    @Test
    fun loadEachResource() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            FakeMusicBrainzApiService(),
            resource,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    resource.toFakeMusicBrainzModel().toListItemModel()
                ),
                prevKey = null,
                nextKey = 1
            ),
            pagingSource.load(
                PagingSource.LoadParams.Append(key = 0, loadSize = 1, placeholdersEnabled = false)
            )
        )

        // if we try to load another page, it will always spit out the same page since we've mocked our api service
        // also can't filter
    }
}
