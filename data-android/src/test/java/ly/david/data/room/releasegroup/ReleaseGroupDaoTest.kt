package ly.david.data.room.releasegroup

import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.HiltTest
import ly.david.data.test.fakeReleaseGroupWithArtistCredits
import ly.david.data.room.artist.credit.ArtistCreditNameRoomModel
import ly.david.data.room.artist.credit.ArtistCreditNamesWithEntity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

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

        var releaseGroupWithArtistCredits = releaseGroupDao.getReleaseGroup("1")
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`("name"))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithEntities, `is`(listOf()))

        releaseGroupWithArtistCredits = releaseGroupDao.getReleaseGroup("2")
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`("blah"))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithEntities, `is`(listOf()))
    }

    @Test
    fun `get release group with artist credits`() = runTest {
        releaseGroupDao.insertReleaseGroupWithArtistCredits(fakeReleaseGroupWithArtistCredits)

        val releaseGroupWithArtistCredits =
            releaseGroupDao.getReleaseGroup(fakeReleaseGroupWithArtistCredits.id)
        assertThat(releaseGroupWithArtistCredits?.releaseGroup?.name, `is`(fakeReleaseGroupWithArtistCredits.name))
        assertThat(releaseGroupWithArtistCredits?.artistCreditNamesWithEntities?.size, `is`(2))
        assertThat(
            releaseGroupWithArtistCredits?.artistCreditNamesWithEntities?.get(0),
            `is`(
                ArtistCreditNamesWithEntity(
                    entityId = "fakeReleaseGroup2",
                    artistCreditNameRoomModel = ArtistCreditNameRoomModel(
                        artistCreditId = 1,
                        position = 0,
                        artistId = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                        name = "Different Artist Name",
                        joinPhrase = " & "
                    )
                )
            )
        )
        assertThat(
            releaseGroupWithArtistCredits?.artistCreditNamesWithEntities?.get(1),
            `is`(
                ArtistCreditNamesWithEntity(
                    entityId = "fakeReleaseGroup2",
                    artistCreditNameRoomModel = ArtistCreditNameRoomModel(
                        artistCreditId = 1,
                        position = 1,
                        artistId = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                        name = "Other Artist",
                        joinPhrase = null
                    )
                )
            )
        )
    }
}
