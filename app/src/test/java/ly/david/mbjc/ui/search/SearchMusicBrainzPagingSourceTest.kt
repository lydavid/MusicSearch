package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.listitem.toListItemModel
import ly.david.data.domain.paging.SearchMusicBrainzPagingSource
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.FakeMusicBrainzApiService
import ly.david.data.network.searchableEntities
import ly.david.data.network.toFakeMusicBrainzModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class SearchMusicBrainzPagingSourceTest(private val entity: MusicBrainzEntity) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return searchableEntities
        }
    }

    @Test
    fun loadEachEntity() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            FakeMusicBrainzApiService(),
            entity,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    entity.toFakeMusicBrainzModel().toListItemModel()
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
