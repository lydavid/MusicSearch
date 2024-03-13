package ly.david.mbjc.ui.event

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.data.test.fakeEvent
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.event.EventRepository
import ly.david.musicsearch.shared.feature.details.event.EventUi
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class EventScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val eventRepository: EventRepository by inject()

    private fun setEvent(eventMusicBrainzModel: EventMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                EventUi(eventId = eventMusicBrainzModel.id)
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

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setEvent(fakeEvent)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(fakeEvent.relations?.first()?.area?.name!!)
    }
}
