package ly.david.data.persistence.label

import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.HiltTest
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseRoomModel
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseLabelDaoTest : HiltTest() {

    @Inject
    lateinit var releaseLabelDao: ReleaseLabelDao
    @Inject
    lateinit var releaseDao: ReleaseDao
    @Inject
    lateinit var labelDao: LabelDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `getNumberOfReleasesByLabel - only count distinct release & label pairs`() = runTest {
        releaseDao.insert(
            ReleaseRoomModel(
                id = "release1",
                name = "Release"
            )
        )
        labelDao.insert(
            LabelRoomModel(
                id = "label1",
                name = "Label",
            )
        )
        releaseLabelDao.insertAll(
            listOf(
                ReleaseLabel(
                    releaseId = "release1",
                    labelId = "label1",
                    catalogNumber = "cat1"
                ),
                ReleaseLabel(
                    releaseId = "release1",
                    labelId = "label1",
                    catalogNumber = "cat2"
                ),
            )
        )

        assertThat(releaseLabelDao.getNumberOfReleasesByLabel("label1"), `is`(1))
    }
}
