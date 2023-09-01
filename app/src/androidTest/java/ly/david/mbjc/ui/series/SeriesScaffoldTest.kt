package ly.david.mbjc.ui.series

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.series.SeriesRepository
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.fakeInstrument
import ly.david.data.network.fakeSeries
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class SeriesScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var seriesRepository: SeriesRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setSeries(seriesMusicBrainzModel: SeriesMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                SeriesScaffold(seriesId = seriesMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setSeries(fakeSeries)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        seriesRepository.lookupSeries(fakeSeries.id)
        setSeries(fakeSeries)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeSeries.getNameWithDisambiguation())

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setSeries(fakeSeries)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
    }
}
