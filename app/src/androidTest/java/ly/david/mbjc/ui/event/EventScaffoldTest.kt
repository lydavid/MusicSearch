package ly.david.mbjc.ui.event

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.event.EventRepository
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.fakeEvent
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class EventScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var eventRepository: EventRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setEvent(eventMusicBrainzModel: EventMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                EventScaffold(eventId = eventMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setEvent(fakeEvent)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        eventRepository.lookupEvent(fakeEvent.id)
        setEvent(fakeEvent)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeEvent.getNameWithDisambiguation())

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setEvent(fakeEvent)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeEvent.relations?.first()?.area?.name!!)
    }
}
