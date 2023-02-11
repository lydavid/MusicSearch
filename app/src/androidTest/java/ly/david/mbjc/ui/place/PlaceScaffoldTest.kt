package ly.david.mbjc.ui.place

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.formatForDisplay
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.fakePlaceWithAllData
import ly.david.data.repository.PlaceRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
        composeTestRule.onRoot(useUnmergedTree = true).printToLog(this::class.java.simpleName)
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setPlace(fakePlaceWithAllData)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        placeRepository.lookupPlace(fakePlaceWithAllData.id)
        setPlace(fakePlaceWithAllData)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.getNameWithDisambiguation())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(details)
            .performClick()
        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.area!!.name)
            .assertIsDisplayed()
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
    }

    @Test
    fun hasRelations() = runTest {
        setPlace(fakePlaceWithAllData)
        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        val relatedEventName = fakePlaceWithAllData.relations?.first()?.event?.name!!

        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodesWithText(relatedEventName)
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithText(relatedEventName)
            .assertIsDisplayed()
    }
}
