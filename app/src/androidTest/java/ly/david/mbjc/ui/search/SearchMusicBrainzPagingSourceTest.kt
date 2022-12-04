package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.toArtistListItemModel
import ly.david.data.domain.toReleaseGroupListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.fakeArtist
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.paging.SearchMusicBrainzPagingSource
import org.junit.Assert.assertEquals
import org.junit.Test

// TODO: hilt and can this be a unit test?
internal class SearchMusicBrainzPagingSourceTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadOneArtist() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            ly.david.data.network.api.FakeMusicBrainzApiService(),
            MusicBrainzResource.ARTIST,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    fakeArtist.toArtistListItemModel()
                ),
                prevKey = 1,
                nextKey = 2
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(key = 1, loadSize = 1, placeholdersEnabled = false)
            )
        )

        // if we try to load another page, it will always spit out the same page since we've mocked our api service
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadOneReleaseGroup() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            ly.david.data.network.api.FakeMusicBrainzApiService(),
            MusicBrainzResource.RELEASE_GROUP,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    fakeReleaseGroup.toReleaseGroupListItemModel()
                ),
                prevKey = 1,
                nextKey = 2
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(key = 1, loadSize = 1, placeholdersEnabled = false)
            )
        )
    }
}
