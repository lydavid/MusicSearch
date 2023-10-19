package ly.david.mbjc.ui.instrument

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.test.fakeInstrument
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.instrument.InstrumentRepository
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class InstrumentScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val instrumentRepository: InstrumentRepository by inject()

    private fun setInstrument(instrumentMusicBrainzModel: InstrumentMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                InstrumentScaffold(instrumentId = instrumentMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setInstrument(fakeInstrument)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        instrumentRepository.lookupInstrument(fakeInstrument.id)
        setInstrument(fakeInstrument)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeInstrument.getNameWithDisambiguation())

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setInstrument(fakeInstrument)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
    }
}
