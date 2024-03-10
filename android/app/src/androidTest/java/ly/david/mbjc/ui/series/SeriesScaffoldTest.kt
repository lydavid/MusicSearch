package ly.david.mbjc.ui.series

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.data.test.fakeInstrument
import ly.david.data.test.fakeSeries
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.series.SeriesRepository
import ly.david.musicsearch.shared.feature.details.series.SeriesScaffold
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class SeriesScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val seriesRepository: SeriesRepository by inject()

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

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setSeries(fakeSeries)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
    }
}
