package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.toArtistUiModel
import ly.david.data.domain.toReleaseGroupUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.paging.SearchMusicBrainzPagingSource
import ly.david.mbjc.data.network.api.FakeMusicBrainzApiService
import ly.david.mbjc.data.network.artistMusicBrainzModel
import ly.david.mbjc.data.network.releaseGroupMusicBrainzModel
import org.junit.Assert.assertEquals
import org.junit.Test

// TODO: hilt and can this be a unit test?
internal class SearchMusicBrainzPagingSourceTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadOneArtist() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            FakeMusicBrainzApiService(),
            MusicBrainzResource.ARTIST,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    artistMusicBrainzModel.toArtistUiModel()
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
            FakeMusicBrainzApiService(),
            MusicBrainzResource.RELEASE_GROUP,
            ""
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    releaseGroupMusicBrainzModel.toReleaseGroupUiModel()
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
