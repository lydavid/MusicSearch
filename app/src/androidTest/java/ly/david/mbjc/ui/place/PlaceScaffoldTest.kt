package ly.david.mbjc.ui.place

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.formatForDisplay
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.fakeEvent
import ly.david.data.network.fakePlaceWithAllData
import ly.david.data.repository.PlaceRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class PlaceScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var placeRepository: PlaceRepository

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
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.address)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.coordinates?.formatForDisplay()!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.type!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.lifeSpan?.begin!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.lifeSpan?.end!!)
            .assertIsDisplayed()

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
