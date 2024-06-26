package ly.david.musicsearch.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeMusicBrainzApi
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.searchableEntities
import ly.david.musicsearch.data.repository.internal.paging.SearchMusicBrainzPagingSource
import ly.david.musicsearch.data.repository.internal.paging.toListItemModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class SearchMusicBrainzPagingSourceTest(private val entity: MusicBrainzEntity) {

    @Test
    fun loadEachEntity() = runTest {
        val pagingSource = SearchMusicBrainzPagingSource(
            FakeMusicBrainzApi(),
            entity,
            "",
        )
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(
                    entity.toFakeMusicBrainzModel().toListItemModel(),
                ),
                prevKey = null,
                nextKey = 1,
            ),
            pagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false,
                ),
            ),
        )

        // if we try to load another page, it will always spit out the same page since we've mocked our api service
        // also can't filter
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return searchableEntities
        }
    }
}
