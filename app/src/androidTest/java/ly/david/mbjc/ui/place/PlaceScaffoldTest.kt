package ly.david.mbjc.ui.place

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.test.fakeEvent
import ly.david.data.test.fakePlaceWithAllData
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.place.PlaceRepository
import ly.david.musicsearch.data.core.place.formatForDisplay
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class PlaceScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val placeRepository: PlaceRepository by inject()

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

        waitForThenPerformClickOn(strings.events)
        waitForThenAssertAtLeastOneIsDisplayed(fakeEvent.name)

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.events).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setPlace(fakePlaceWithAllData)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertAtLeastOneIsDisplayed(fakePlaceWithAllData.relations?.first()?.event?.name!!)
    }
}
