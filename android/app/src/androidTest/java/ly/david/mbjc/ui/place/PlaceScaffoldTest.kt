package ly.david.mbjc.ui.place

//internal class PlaceScaffoldTest : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//    private val placeRepository: PlaceRepository by inject()
//
//    private fun setPlace(placeMusicBrainzModel: PlaceMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                PlaceScaffold(placeId = placeMusicBrainzModel.id)
//            }
//        }
//    }
//
//    @Test
//    fun firstVisit_noLocalData() = runTest {
//        setPlace(fakePlaceWithAllData)
//
//        assertFieldsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() = runTest {
//        placeRepository.lookupPlace(fakePlaceWithAllData.id)
//        setPlace(fakePlaceWithAllData)
//
//        assertFieldsDisplayed()
//    }
//
//    private fun assertFieldsDisplayed() {
//        waitForThenAssertIsDisplayed(fakePlaceWithAllData.getNameWithDisambiguation())
//
//        waitForThenAssertIsDisplayed(fakePlaceWithAllData.area!!.name)
//        waitForThenAssertIsDisplayed("Address: ${fakePlaceWithAllData.address}")
//        waitForThenAssertIsDisplayed(fakePlaceWithAllData.coordinates?.formatForDisplay()!!)
//        waitForThenAssertIsDisplayed("Type: ${fakePlaceWithAllData.type!!}")
//        waitForThenAssertIsDisplayed("Opened: ${fakePlaceWithAllData.lifeSpan?.begin!!}")
//        waitForThenAssertIsDisplayed("Closed: ${fakePlaceWithAllData.lifeSpan?.end!!}")
//
//        waitForThenPerformClickOn(strings.events)
//        waitForThenAssertAtLeastOneIsDisplayed(fakeEvent.name)
//
//        waitForThenPerformClickOn(strings.stats)
//        waitForThenAssertIsDisplayed(hasText(strings.events).and(hasNoClickAction()))
//        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
//    }
//
//    @Test
//    fun hasRelations() = runTest {
//        setPlace(fakePlaceWithAllData)
//
//        waitForThenPerformClickOn(strings.relationships)
//        waitForThenAssertAtLeastOneIsDisplayed(fakePlaceWithAllData.relations?.first()?.event?.name!!)
//    }
//}
