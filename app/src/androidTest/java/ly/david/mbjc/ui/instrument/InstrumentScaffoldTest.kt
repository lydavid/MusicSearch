package ly.david.mbjc.ui.instrument

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.domain.instrument.InstrumentRepository
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.test.fakeInstrument
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

internal class InstrumentScaffoldTest : MainActivityTest(), StringReferences, KoinTest {

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

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setInstrument(fakeInstrument)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeInstrument.relations?.first()?.area?.name!!)
    }
}
