package ly.david.mbjc.data.persistence

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.io.IOException
import kotlinx.coroutines.runBlocking
import ly.david.mbjc.data.network.SEARCH_BROWSE_LIMIT
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ReleaseGroupDaoTest {

    // TODO: Should use a test database if we need mocking
    //  otherwise we can just use this, since it doesn't actually alter our real database.
    private lateinit var db: MusicBrainzRoomDatabase
    private lateinit var releaseGroupDao: ReleaseGroupDao
    private lateinit var releaseGroupArtistDao: ReleaseGroupArtistDao
    private lateinit var artistDao: ArtistDao

    // TODO: do this for all daos in a parent class
    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MusicBrainzRoomDatabase::class.java
        ).build()
        releaseGroupDao = db.getReleaseGroupDao()
        releaseGroupArtistDao = db.getReleaseGroupArtistDao()
        artistDao = db.getArtistDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupById() {
        runBlocking {
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
            assertEquals(foundReleaseGroup?.name, "name")
        }
    }

    @Test
    @Throws(Exception::class)
    fun getReleaseGroupsByArtist() {
        runBlocking {
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
                        name = "Should fine me"
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

            val pagingSource = releaseGroupDao.getReleaseGroupsByArtist("a1")
            val loadResult: PagingSource.LoadResult<Int, ReleaseGroupRoomModel> = pagingSource.load(
                PagingSource.LoadParams.Refresh(key = null, loadSize = SEARCH_BROWSE_LIMIT, placeholdersEnabled = false)
            )
            val foundReleaseGroups = (loadResult as PagingSource.LoadResult.Page<Int, ReleaseGroupRoomModel>).data

            assertEquals(
                "Should fine me",
                foundReleaseGroups[0].name
            )
            assertEquals(
                "and me",
                foundReleaseGroups[1].name
            )
        }
    }
}
