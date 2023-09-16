package ly.david.data.room.artist

import android.content.Context
import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import ly.david.data.di.room.roomDatabaseDaoModule
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroup
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroupDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.data.room.releasegroup.ReleaseGroupForListItem
import ly.david.data.room.releasegroup.ReleaseGroupRoomModel
import ly.david.data.room.testDatabaseModule
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ArtistReleaseGroupDaoTest : KoinTest {

    private val artistDao: ArtistDao by inject()
    private val artistReleaseGroupDao: ArtistReleaseGroupDao by inject()
    private val releaseGroupDao: ReleaseGroupDao by inject()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            modules(
                roomDatabaseDaoModule,
                testDatabaseModule,
                module {
                    single<Context> {
                        context
                    }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
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

        assertThat("Should find me", `is`(releaseGroupWithArtistCredits[0].releaseGroup.name))
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
            artistReleaseGroupDao.getReleaseGroupsByArtist(artistId = "a1", sorted = true)
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
            artistReleaseGroupDao.getReleaseGroupsByArtist("a1", "%f%")
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
            artistReleaseGroupDao.getReleaseGroupsByArtist(artistId = "a1", query = "%me%", sorted = true)
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
            artistReleaseGroupDao.getReleaseGroupsByArtist("a1", "%Album%")
        )

        assertEquals(2, releaseGroupWithArtistCredits.size)
        assertEquals("blah", releaseGroupWithArtistCredits[0].releaseGroup.name)
        assertEquals("and me", releaseGroupWithArtistCredits[1].releaseGroup.name)
    }

    @Test
    fun `delete release groups by artist`() = runTest {
        artistDao.insertAll(
            listOf(
                ArtistRoomModel(
                    id = "a1",
                    name = "Artist"
                ),
                ArtistRoomModel(
                    id = "a2",
                    name = "Other Artist"
                ),
            )
        )
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "rg1",
                    name = "I will be deleted",
                    primaryType = "Single"
                ),
                ReleaseGroupRoomModel(
                    id = "rg2",
                    name = "so will I, even though I belong to another artist as well",
                    primaryType = "Album"
                ),
                ReleaseGroupRoomModel(
                    id = "rg3",
                    name = "I'm still here",
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
                    releaseGroupId = "rg2",
                    artistId = "a2",
                ),
                ArtistReleaseGroup(
                    releaseGroupId = "rg3",
                    artistId = "a2"
                )
            )
        )

        artistReleaseGroupDao.deleteReleaseGroupsByArtist("a1")
        var releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtist("a1")
        )
        assertEquals(0, releaseGroupWithArtistCredits.size)

        releaseGroupWithArtistCredits = getReleaseGroupsFromPagingSource(
            artistReleaseGroupDao.getReleaseGroupsByArtist("a2")
        )
        assertEquals(1, releaseGroupWithArtistCredits.size)
        assertEquals("I'm still here", releaseGroupWithArtistCredits[0].releaseGroup.name)
    }

    // TODO: generic
    private suspend fun getReleaseGroupsFromPagingSource(
        pagingSource: PagingSource<Int, ReleaseGroupForListItem>,
    ): List<ReleaseGroupForListItem> {
        val loadResult: PagingSource.LoadResult<Int, ReleaseGroupForListItem> = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        return (loadResult as PagingSource.LoadResult.Page<Int, ReleaseGroupForListItem>).data
    }
}
