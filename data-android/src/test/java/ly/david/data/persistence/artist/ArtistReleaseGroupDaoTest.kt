package ly.david.data.persistence.artist

import androidx.paging.PagingSource
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.HiltTest
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupWithArtistCredits
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ArtistReleaseGroupDaoTest : HiltTest() {

    @Inject
    lateinit var artistDao: ArtistDao

    @Inject
    lateinit var artistReleaseGroupDao: ArtistReleaseGroupDao

    @Inject
    lateinit var releaseGroupDao: ReleaseGroupDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `get release groups by artist`() = runTest {
        artistDao.insert(
            ArtistRoomModel(
                id = "a1",
                name = "Artist"
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "Should find me"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "and me"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "but not me"
                ),
            )
        )
        artistReleaseGroupDao.insertAll(
            listOf(
                ArtistReleaseGroup(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                )
            )
        )

        val releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtist("a1")
        )

        assertThat("Should find me", `is`(releaseGroupWithArtistCredits[0].releaseGroup.name)
        )
        assertThat("and me", `is`(releaseGroupWithArtistCredits[1].releaseGroup.name))
    }

    @Test
    fun `get release groups by artist sorted by primary type`() = runTest {
        artistDao.insert(
            ArtistRoomModel(
                id = "a1",
                name = "Artist"
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "Should find me",
                    primaryType = "Single"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "and me",
                    primaryType = "Album"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "me too"
                ),
            )
        )
        artistReleaseGroupDao.insertAll(
            listOf(
                ArtistReleaseGroup(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg3",
                    artistId = "a1"
                )
            )
        )

        val releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtistSorted("a1")
        )

        assertEquals(3, releaseGroupWithArtistCredits.size)
        assertEquals("me too", releaseGroupWithArtistCredits[0].releaseGroup.name)
        assertEquals("and me", releaseGroupWithArtistCredits[1].releaseGroup.name)
        assertEquals("Should find me", releaseGroupWithArtistCredits[2].releaseGroup.name)
    }

    @Test
    fun `get release groups by artist filtered on name`() = runTest {
        artistDao.insert(
            ArtistRoomModel(
                id = "a1",
                name = "Artist"
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "Should find me"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "and me"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "but not me"
                ),
            )
        )
        artistReleaseGroupDao.insertAll(
            listOf(
                ArtistReleaseGroup(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg3",
                    artistId = "a1"
                )
            )
        )

        val releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtistFiltered("a1", "%f%")
        )

        assertEquals(1, releaseGroupWithArtistCredits.size)
        assertEquals("Should find me", releaseGroupWithArtistCredits[0].releaseGroup.name)
    }

    @Test
    fun `get release groups by artist filtered and sorted`() = runTest {
        artistDao.insert(
            ArtistRoomModel(
                id = "a1",
                name = "Artist"
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "Should find me",
                    primaryType = "Single"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "blah",
                    primaryType = "Album"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "and me",
                    primaryType = "Album"
                ),
            )
        )
        artistReleaseGroupDao.insertAll(
            listOf(
                ArtistReleaseGroup(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg3",
                    artistId = "a1"
                )
            )
        )

        val releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtistFilteredSorted("a1", "%me%")
        )

        assertEquals(2, releaseGroupWithArtistCredits.size)
        assertEquals("and me", releaseGroupWithArtistCredits[0].releaseGroup.name)
        assertEquals("Should find me", releaseGroupWithArtistCredits[1].releaseGroup.name)
    }

    @Test
    fun `get release groups by artist filtered on primary type`() = runTest {
        artistDao.insert(
            ArtistRoomModel(
                id = "a1",
                name = "Artist"
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "Should find me",
                    primaryType = "Single"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "blah",
                    primaryType = "Album"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "and me",
                    primaryType = "Album"
                ),
            )
        )
        artistReleaseGroupDao.insertAll(
            listOf(
                ArtistReleaseGroup(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg3",
                    artistId = "a1"
                )
            )
        )

        val releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtistFilteredSorted("a1", "%Album%")
        )

        assertEquals(2, releaseGroupWithArtistCredits.size)
        assertEquals("blah", releaseGroupWithArtistCredits[0].releaseGroup.name)
        assertEquals("and me", releaseGroupWithArtistCredits[1].releaseGroup.name)
    }

    // TODO: generic
    private suspend fun getReleaseGroupsFromPagingSource(
        pagingSource: PagingSource<Int, ReleaseGroupWithArtistCredits>
    ): List<ReleaseGroupWithArtistCredits> {
        val loadResult: PagingSource.LoadResult<Int, ReleaseGroupWithArtistCredits> = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        return (loadResult as PagingSource.LoadResult.Page<Int, ReleaseGroupWithArtistCredits>).data
    }
}
