package ly.david.data.room.label

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import ly.david.data.di.room.databaseDaoModule
import ly.david.data.room.label.releases.ReleaseLabel
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseRoomModel
import ly.david.data.room.testDatabaseModule
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
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
internal class ReleaseLabelDaoTest : KoinTest {

    private val releaseLabelDao: ReleaseLabelDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val labelDao: LabelDao by inject()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            modules(
                databaseDaoModule,
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
