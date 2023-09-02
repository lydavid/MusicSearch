package ly.david.mbjc.ui.place

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.place.PlaceRepository
import ly.david.data.formatForDisplay
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.test.fakeEvent
import ly.david.data.test.fakePlaceWithAllData
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class PlaceScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var placeRepository: PlaceRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setPlace(placeMusicBrainzModel: PlaceMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                PlaceScaffold(placeId = placeMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setPlace(fakePlaceWithAllData)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        placeRepository.lookupPlace(fakePlaceWithAllData.id)
        setPlace(fakePlaceWithAllData)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakePlaceWithAllData.getNameWithDisambiguation())

        waitForThenAssertIsDisplayed(fakePlaceWithAllData.area!!.name)
        waitForThenAssertIsDisplayed("Address: ${fakePlaceWithAllData.address}")
        waitForThenAssertIsDisplayed(fakePlaceWithAllData.coordinates?.formatForDisplay()!!)
        waitForThenAssertIsDisplayed("Type: ${fakePlaceWithAllData.type!!}")
        waitForThenAssertIsDisplayed("Opened: ${fakePlaceWithAllData.lifeSpan?.begin!!}")
        waitForThenAssertIsDisplayed("Closed: ${fakePlaceWithAllData.lifeSpan?.end!!}")

        waitForThenPerformClickOn(events)
        waitForThenAssertAtLeastOneIsDisplayed(fakeEvent.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(events).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setPlace(fakePlaceWithAllData)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertAtLeastOneIsDisplayed(fakePlaceWithAllData.relations?.first()?.event?.name!!)
    }
}
