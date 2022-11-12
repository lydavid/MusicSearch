package ly.david.data.persistence.releasegroup

import androidx.paging.PagingSource
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.HiltTest
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.ArtistRoomModel
import ly.david.data.persistence.artist.ReleaseGroupArtistCreditRoomModel
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// TODO: splitting data by data and data-android means we are testing this
//  in data-android, despite this dao being from data
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseGroupDaoTest : HiltTest() {

    @Inject
    lateinit var db: MusicBrainzDatabase

    @Inject
    lateinit var releaseGroupDao: ReleaseGroupDao

    @Inject
    lateinit var releaseGroupArtistDao: ReleaseGroupArtistDao

    @Inject
    lateinit var artistDao: ArtistDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupById() = runTest {
        releaseGroupDao.insertAll(
            listOf(
                ReleaseGroupRoomModel(
                    id = "1",
                    name = "name"
                ),
                ReleaseGroupRoomModel(
                    id = "2",
                    name = "blah"
                )
            )
        )

        val foundReleaseGroup = releaseGroupDao.getReleaseGroup("1")
        assertThat(foundReleaseGroup?.name, `is`("name"))
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupsByArtist() = runTest {
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
        releaseGroupArtistDao.insertAll(
            listOf(
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                ),
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                )
            )
        )

        val foundReleaseGroups = getReleaseGroupsFromPagingSource(
            releaseGroupDao.getReleaseGroupsByArtist("a1")
        )

        assertEquals(
            "Should find me",
            foundReleaseGroups[0].name
        )
        assertEquals(
            "and me",
            foundReleaseGroups[1].name
        )
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupsByArtistSortedByPrimaryType() = runTest {
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
                    name = "but not me"
                ),
            )
        )
        releaseGroupArtistDao.insertAll(
            listOf(
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                ),
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                )
            )
        )

        val foundReleaseGroups = getReleaseGroupsFromPagingSource(
            releaseGroupDao.getReleaseGroupsByArtistSorted("a1")
        )

        assertEquals(
            "and me",
            foundReleaseGroups[0].name
        )
        assertEquals(
            "Should find me",
            foundReleaseGroups[1].name
        )
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupsByArtistFiltered() = runTest {
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
        releaseGroupArtistDao.insertAll(
            listOf(
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg1",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                ),
                ReleaseGroupArtistCreditRoomModel(
                    releaseGroupId = "rg2",
                    artistId = "a1",
                    name = "Artist name in this release group",
                    joinPhrase = "",
                    order = 0
                )
            )
        )

        val foundReleaseGroups = getReleaseGroupsFromPagingSource(
            releaseGroupDao.getReleaseGroupsByArtistFiltered("a1", "%f%")
        )

        assertEquals(
            "Should find me",
            foundReleaseGroups[0].name
        )
        assertEquals(
            1,
            foundReleaseGroups.size
        )
    }

    // TODO: filtered and sorted

    // TODO: filter on fields other than name

    // TODO: sort on other fields

    private suspend fun getReleaseGroupsFromPagingSource(
        pagingSource: PagingSource<Int, ReleaseGroupRoomModel>
    ): List<ReleaseGroupRoomModel> {
        val loadResult: PagingSource.LoadResult<Int, ReleaseGroupRoomModel> = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        return (loadResult as PagingSource.LoadResult.Page<Int, ReleaseGroupRoomModel>).data
    }
}
