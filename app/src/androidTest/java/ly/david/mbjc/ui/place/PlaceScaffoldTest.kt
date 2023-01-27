package ly.david.mbjc.ui.place

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
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
    fun firstVisit_noLocalData() {
        setPlace(fakePlaceWithAllData)
        runBlocking { composeTestRule.awaitIdle() }

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() {
        runBlocking {
            placeRepository.lookupPlace(fakePlaceWithAllData.id)
            setPlace(fakePlaceWithAllData)
            composeTestRule.awaitIdle()
        }

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
    fun hasRelations() {
        setPlace(fakePlaceWithAllData)
        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        composeTestRule
            .onNodeWithText(fakePlaceWithAllData.relations?.first()?.event?.name ?: "")
            .assertIsDisplayed()
    }
}
