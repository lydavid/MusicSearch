package ly.david.mbjc.ui.instrument

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.fakeInstrument
import ly.david.data.repository.InstrumentRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class InstrumentScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var instrumentRepository: InstrumentRepository

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
