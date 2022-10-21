package ly.david.mbjc.ui.search

import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.ArtistUiModel
import ly.david.data.domain.ReleaseGroupUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.paging.SearchMusicBrainzPagingSource
import ly.david.mbjc.data.network.api.FakeMusicBrainzApiService
import org.junit.Assert.assertEquals
import org.junit.Test

// TODO: hilt
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
                    ArtistUiModel(
                        id = "1",
                        name = "artist name",
                        sortName = "sort name"
                    )
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
                    ReleaseGroupUiModel(
                        id = "1",
                        name = "release group name",
                        firstReleaseDate = "2022-03-14"
                    )
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
