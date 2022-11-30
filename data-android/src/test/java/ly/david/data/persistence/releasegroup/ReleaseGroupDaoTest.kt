package ly.david.data.persistence.releasegroup

import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.HiltTest
import ly.david.data.network.fakeReleaseGroupWithArtistCredits
import ly.david.data.persistence.artist.credit.ArtistCreditNameRoomModel
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

// TODO: splitting data by data and data-android means we are testing this
//  in data-android, despite this dao being from data
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseGroupDaoTest : HiltTest() {

    @Inject
    lateinit var releaseGroupDao: ReleaseGroupDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `get release group without inserting artist credits`() = runTest {
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

        var releaseGroupWithArtistCredits = releaseGroupDao.getReleaseGroupWithArtistCredits("1")
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`("name"))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithResources, `is`(listOf()))

        releaseGroupWithArtistCredits = releaseGroupDao.getReleaseGroupWithArtistCredits("2")
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`("blah"))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithResources, `is`(listOf()))
    }

    @Test
    fun `get release group with artist credits`() = runTest {
        releaseGroupDao.insertReleaseGroupWithArtistCredits(fakeReleaseGroupWithArtistCredits)

        val releaseGroupWithArtistCredits =
            releaseGroupDao.getReleaseGroupWithArtistCredits(fakeReleaseGroupWithArtistCredits.id)
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`(fakeReleaseGroupWithArtistCredits.name))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithResources?.size, `is`(2))
        assertThat(
            releaseGroupWithArtistCredits?.artistCreditNamesWithResources?.get(0),
            `is`(
                ArtistCreditNamesWithResource(
                    resourceId = "fakeReleaseGroup2",
                    artistCreditNameRoomModel = ArtistCreditNameRoomModel(
                        artistCreditId = 1,
                        position = 0,
                        artistId = "artist1",
                        name = "Different Artist Name",
                        joinPhrase = " feat. "
                    )
                )
            )
        )
        assertThat(
            releaseGroupWithArtistCredits?.artistCreditNamesWithResources?.get(1),
            `is`(
                ArtistCreditNamesWithResource(
                    resourceId = "fakeReleaseGroup2",
                    artistCreditNameRoomModel = ArtistCreditNameRoomModel(
                        artistCreditId = 1,
                        position = 1,
                        artistId = "artist2",
                        name = "Other Artist",
                        joinPhrase = null
                    )
                )
            )
        )
    }
}
